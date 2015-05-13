package com.juanma.alcoholgames;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juanma.alcoholgames.adapter.GameArrayAdapter;
import com.juanma.alcoholgames.utils.GamesInfoNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private GameArrayAdapter gamesAdapter;
    private ListView gamesListView = null;

    private static final int GAME_ACTIVITY = 1;
    private static final int FAVORITES_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        TODO FEATURE: Buscador por nombre/tags
        TODO!!! FEATURE: Favoritos activity
        TODO FEATURE: Editar/agregar juego (se guarda localmente)
         */

        // View and ArrayList containing the games:
        gamesListView = (ListView) findViewById(R.id.gamesListView);
        ArrayList<Game> gamesList = new ArrayList<Game>();

        // Now I open the JSON File and then add all the games to the gamesList
        String JSONFile = loadJSONGamesInfo();
        gamesList = getGamesFromJSON(JSONFile);

        // Then, I create an adapter and set it to the ListView
        gamesAdapter = new GameArrayAdapter(this, R.layout.game_list_view, gamesList);
        gamesListView.setAdapter(gamesAdapter);

        // Finally, the click listener for the ListView
        gamesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game actualGame = (Game) gamesListView.getItemAtPosition(position);
                openGame(actualGame, position);
            }
        });

        // TODO FEATURE: Hold-click opens menu with option to fav
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // ActionBar's search item listener:

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.favorites:
            openFavorites();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureID, Menu menu) {
        if(featureID == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                // Le digo al overflow menu que despliegue los iconos:
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureID, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.GAME_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Game selectedGame, toModifyGame;
                    int position;

                    selectedGame = (Game) data.getExtras().getSerializable(GamesInfoNames.GAME_OBJ);
                    position = data.getExtras().getInt(GamesInfoNames.GAME_POSITION);
                    toModifyGame = gamesAdapter.getItem(position);
                    toModifyGame.copyFrom(selectedGame);
                }
                break;

            case MainActivity.FAVORITES_ACTIVITY:

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gamesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        try {
            // First I erase the file:
            File filePath = new File(Environment.getExternalStorageDirectory(), "AlcoholGames");
            new PrintWriter( new File(filePath, GamesInfoNames.JSON_FILENAME) ).close();

            // Now I'll make the JSONObject containing the games:
            JSONObject games = new JSONObject();
            JSONArray gamesArray = new JSONArray();
            for(int i=0; i < gamesAdapter.getCount(); ++i) {
                gamesArray.put(gamesAdapter.getItem(i).toJSONObject());
            }
            games.put(GamesInfoNames.GAMES_ARRAY, gamesArray);

            // Finally I write the JSONObject to the file
            File gamesFile = new File(filePath, GamesInfoNames.JSON_FILENAME);
            FileOutputStream output = new FileOutputStream(gamesFile);
            output.write(games.toString().getBytes());
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }


    /**************************************** PRIVATE: ********************************************/

    private void openGame(Game game, int position) {
        // I create the intent depending on the view to be used
        Intent intent;
        intent = new Intent("com.juanma.alcoholgames.GameActivity");

        intent.putExtra(GamesInfoNames.GAME_OBJ, game);
        intent.putExtra(GamesInfoNames.GAME_POSITION, position);

        startActivityForResult(intent, MainActivity.GAME_ACTIVITY);
    }

    private void openFavorites() {
        Intent intent = new Intent("com.juanma.alcoholgames.FavoritesActivity");

        ArrayList<Game> favorites = new ArrayList<Game>();
        for (int i=0; i<gamesAdapter.getCount(); ++i) {
            Game game = gamesAdapter.getItem(i);
            if (game.isFaved())
                favorites.add(game);
        }

        intent.putExtra(GamesInfoNames.FAVORITES, favorites);

        startActivityForResult(intent, MainActivity.FAVORITES_ACTIVITY);
    }

    // Return the JSON file as string
    private String loadJSONGamesInfo() {
        String json;
        try {
            // If the local file does not exist I create a new one from the assets' JSON file
            File newFolder = new File(Environment.getExternalStorageDirectory(), "AlcoholGames");
            File gamesInfoFile = new File(newFolder, GamesInfoNames.JSON_FILENAME);
            if (!gamesInfoFile.exists()) {
                newFolder.mkdirs();
                gamesInfoFile.createNewFile();
                InputStream inputStream = getAssets().open(GamesInfoNames.JSON_FILENAME);
                copyFile(gamesInfoFile, inputStream);
            }

            // Now I'll read the entire file
            FileInputStream gamesInputStream = new FileInputStream(gamesInfoFile);
            int size = gamesInputStream.available();
            byte[] buffer = new byte[size];
            gamesInputStream.read(buffer);
            gamesInputStream.close();

            // And I cast it to String
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void copyFile(File file, InputStream content) {
        try {
            OutputStream fileOutput = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int read;

            read = content.read(buffer);
            while (read != -1) {
                fileOutput.write(buffer, 0, read);
                read = content.read(buffer);
            }

            fileOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Game> getGamesFromJSON(String jsonFile) {
        JSONObject gamesInfoObject = null;
        ArrayList<Game> gamesList = new ArrayList<Game>();

        try {
            gamesInfoObject = new JSONObject(jsonFile);
            JSONArray gamesArray = gamesInfoObject.getJSONArray(GamesInfoNames.GAMES_ARRAY);

            // Creating the games to add them to the list
            for (int i=0; i < gamesArray.length(); i++) {
                Game actualGame = Game.hydrate(gamesArray.getJSONObject(i));
                gamesList.add(actualGame);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        } finally {
            return gamesList;
        }
    }

}
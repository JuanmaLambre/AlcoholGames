package com.juanma.alcoholgames;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    private GameArrayAdapter gamesAdapter, queryAdapter;
    private ListView gamesListView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        TODO: Credits
        TODO FEATURE: Editar/agregar juego (se guarda localmente)
        TODO: Games recovery option
         */

        // View and ArrayList containing the games:
        gamesListView = (ListView) findViewById(R.id.gamesListView);

        // Now I open the JSON File and then add all the games to the gamesList
        String JSONFile = loadJSONGamesInfo();
        GamesList gamesList = GamesList.getInstance();
        gamesList.loadFromJSON(JSONFile);

        // Then, I create an adapter and set it to the ListView
        gamesAdapter = new GameArrayAdapter(this, R.layout.game_list_view, gamesList);
        gamesListView.setAdapter(gamesAdapter);

        // Finally, the click listener for the ListView
        gamesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game actualGame = (Game) gamesListView.getItemAtPosition(position);
                startActivity(GameActivity.createIntent(actualGame.getID()));
            }
        });

        // TODO FEATURE: Hold-click opens menu with option to fav
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // ActionBar's search item listener:
        MenuItem searcherItem = menu.findItem(R.id.action_search);
        final SearchView searcher = (SearchView) searcherItem.getActionView();

        MenuItemCompat.setOnActionExpandListener(searcherItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            // When the searcher expands the ListView adapter changes
            public boolean onMenuItemActionExpand(MenuItem item) {
                GamesList gamesCopy = (GamesList) GamesList.getInstance().getArrayCopy();
                queryAdapter = new GameArrayAdapter(
                        getBaseContext(), R.layout.game_list_view, gamesCopy);
                gamesListView.setAdapter(queryAdapter);
                return true;
            }
            @Override
            // When the searcher collapses the ListView adapter is set to the original
            public boolean onMenuItemActionCollapse(MenuItem item) {
                gamesListView.setAdapter(gamesAdapter);
                return true;
            }
        });

        searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Nothing to do, list refreshes every time a key is pressed
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                filterGamesBy(query.split(" "));
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.favorites:
            startActivity(FavoritesActivity.createIntent());
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

            // I get the JSONObject containing the games...
            JSONObject games = GamesList.getInstance().toJSONObject();

            // Finally I write the JSONObject to the file
            File gamesFile = new File(filePath, GamesInfoNames.JSON_FILENAME);
            FileOutputStream output = new FileOutputStream(gamesFile);
            output.write(games.toString().getBytes());
            output.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.onDestroy();
    }


    /**************************************** PRIVATE: ********************************************/

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
            throw new RuntimeException(e);
        }
    }

    private void filterGamesBy(String[] words) {
        for (Game game : GamesList.getInstance()) {
            if (queryAdapter.includes(game)) {
                if (!game.hasTags(words))
                    queryAdapter.remove(game);
            } else {
                if (game.hasTags(words))
                    queryAdapter.add(game);
            }

        }
    }

}
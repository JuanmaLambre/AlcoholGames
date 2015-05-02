package com.juanma.alcoholgames;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.juanma.alcoholgames.adapter.GameArrayAdapter;
import com.juanma.alcoholgames.utils.GamesInfoNames;
import com.juanma.alcoholgames.utils.Misc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private String JSONFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        TODO FEATURE: Buscador por nombre/tags
        TODO FEATURE: Agregar a favoritos
        TODO FEATURE: Editar/agregar (se guarda localmente)
         */

        // Initializing the list view enumerating the games
        final ListView gamesListView = (ListView) findViewById(R.id.gamesListView);
        ArrayList<Game> gamesList = new ArrayList<Game>();

        // Opening the JSON File, and then add all the games to the gamesList
        try {
            this.JSONFile = Misc.loadJSONFromAsset(GamesInfoNames.JSON_FILENAME, this);
            JSONObject gamesInfoObject = new JSONObject(this.JSONFile);
            JSONArray gamesArray = gamesInfoObject.getJSONArray(GamesInfoNames.GAMES_ARRAY);

            // As I have to save only the ID and the name
            for (int i=0; i < gamesArray.length(); i++) {
                Game actualGame = new Game(
                        gamesArray.getJSONObject(i).getInt(GamesInfoNames.GAME_ID),
                        gamesArray.getJSONObject(i).getString(GamesInfoNames.GAME_TITLE),
                        gamesArray.getJSONObject(i).getString(GamesInfoNames.GAME_DESCRIPTION),
                        gamesArray.getJSONObject(i).getString(GamesInfoNames.GAME_INSTRUCTIONS),
                        gamesArray.getJSONObject(i).getString(GamesInfoNames.GAME_ADDONS)
                );
                gamesList.add(actualGame);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Third, I create an adapter and set it to the ListView
        GameArrayAdapter adapter = new GameArrayAdapter(this, R.layout.game_layout, gamesList);
        gamesListView.setAdapter(adapter);

        // Finally, the click listener for the ListView
        gamesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game actualGame = (Game) gamesListView.getItemAtPosition(position);
                openGame(actualGame, actualGame.hasAddons());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void openGame(Game game, boolean hasAddons) {
        // I create the intent depending on the view to be used
        Intent intent;
        intent = new Intent("com.juanma.alcoholgames.GameActivity");

        Bundle bundle = new Bundle();
        bundle.putSerializable(GamesInfoNames.GAME_OBJ, game);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
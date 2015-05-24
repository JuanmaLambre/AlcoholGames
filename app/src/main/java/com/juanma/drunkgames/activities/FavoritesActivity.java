package com.juanma.drunkgames.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juanma.drunkgames.Game;
import com.juanma.drunkgames.GamesList;
import com.juanma.drunkgames.R;
import com.juanma.drunkgames.adapter.GameArrayAdapter;

import java.util.ArrayList;

public class FavoritesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);

        // ActionBar:
        setTitle("Favoritos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Adding favorites to the ListView:
        final ListView favorites = (ListView) findViewById(R.id.favsListView);
        ArrayList<Game> favsList = GamesList.getInstance().getFavs();
        GameArrayAdapter gamesAdapter = new GameArrayAdapter(
                this, R.layout.game_list_view, favsList);
        favorites.setAdapter(gamesAdapter);

        // Click listener to ListView:
        favorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game actualGame = (Game) favorites.getItemAtPosition(position);
                startActivity(GameActivity.createIntent(actualGame.getID()));
            }
        });
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Home button pressed:
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public static Intent createIntent() {
        return new Intent("com.juanma.drunkgames.activities.FavoritesActivity");
    }
}

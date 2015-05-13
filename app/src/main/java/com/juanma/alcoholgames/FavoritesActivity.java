package com.juanma.alcoholgames;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.juanma.alcoholgames.adapter.GameArrayAdapter;
import com.juanma.alcoholgames.utils.GamesInfoNames;

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
        ListView favorites = (ListView) findViewById(R.id.favsListView);
        ArrayList<Game> favsList = (ArrayList<Game>) getIntent().getExtras().getSerializable(
                GamesInfoNames.FAVORITES);
        GameArrayAdapter gamesAdapter = new GameArrayAdapter(
                this, R.layout.game_list_view, favsList);
        favorites.setAdapter(gamesAdapter);
    }
}

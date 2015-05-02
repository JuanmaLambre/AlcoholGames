package com.juanma.alcoholgames;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.juanma.alcoholgames.utils.GamesInfoNames;


public class GameActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setting the view:
        Game selectedGame = (Game) getIntent().getExtras().getSerializable(GamesInfoNames.GAME_OBJ);
        TextView description, instructions, addons, addonsTitle;

        setTitle(selectedGame.getTitle());

        // Instructions' text
        instructions = (TextView) findViewById(R.id.instructions);
        instructions.setText(selectedGame.getInstructions());

        // Description's text
        description = (TextView) findViewById(R.id.description);
        if ( !selectedGame.hasDescription() ) {
            description.setVisibility(View.GONE);
        }
        else {
            description.setText(selectedGame.getDescription());
        }

        // Add-ons' text
        addons = (TextView) findViewById(R.id.addons);
        addonsTitle = (TextView) findViewById(R.id.addonsTitle);
        if ( !selectedGame.hasAddons() ) {
            addons.setVisibility(View.GONE);
            addonsTitle.setVisibility(View.GONE);
        }
        else {
            addons.setText(selectedGame.getAddons());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

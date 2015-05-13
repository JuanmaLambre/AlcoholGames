package com.juanma.alcoholgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.juanma.alcoholgames.utils.GamesInfoNames;


public class GameActivity extends ActionBarActivity {

    MenuItem favIcon;
    Game currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setting the view:
        currentGame = (Game) getIntent().getExtras().getSerializable(GamesInfoNames.GAME_OBJ);
        TextView description, instructions, addons, addonsTitle;

        // ActionBar:
        setTitle(currentGame.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instructions' text
        instructions = (TextView) findViewById(R.id.instructions);
        instructions.setText(currentGame.getInstructions());

        // Description's text
        description = (TextView) findViewById(R.id.description);
        if ( !currentGame.hasDescription() ) {
            description.setVisibility(View.GONE);
        } else {
            description.setText(currentGame.getDescription());
        }

        // Add-ons' text
        addons = (TextView) findViewById(R.id.addons);
        addonsTitle = (TextView) findViewById(R.id.addonsTitle);
        if ( !currentGame.hasAddons() ) {
            addons.setVisibility(View.GONE);
            addonsTitle.setVisibility(View.GONE);
        } else {
            addons.setText(currentGame.getAddons());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);
        this.favIcon = menu.findItem(R.id.fav_icon);

        if (currentGame.isFaved()) {
            this.favIcon.setIcon(R.drawable.star_on);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Fav icon pressed:
        case R.id.fav_icon:
            if (currentGame.isFaved()) {
                currentGame.unfav();
                favIcon.setIcon(R.drawable.star_off);
            } else {
                currentGame.fav();
                favIcon.setIcon(R.drawable.star_on);
                Toast.makeText(this, "Faved", Toast.LENGTH_SHORT).show();
            }
            break;
        // Home button pressed:
        case android.R.id.home:
            onBackPressed();
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int gamePosition = getIntent().getExtras().getInt(GamesInfoNames.GAME_POSITION);

        Intent gameIntent = new Intent();
        gameIntent.putExtra(GamesInfoNames.GAME_OBJ, currentGame);
        gameIntent.putExtra(GamesInfoNames.GAME_POSITION, gamePosition);
        setResult(RESULT_OK, gameIntent);
        finish();
    }
}

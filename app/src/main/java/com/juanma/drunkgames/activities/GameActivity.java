package com.juanma.drunkgames.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.juanma.drunkgames.Game;
import com.juanma.drunkgames.GamesList;
import com.juanma.drunkgames.R;
import com.juanma.drunkgames.utils.GamesInfoNames;


public class GameActivity extends ActionBarActivity {

    MenuItem favIcon;
    Game currentGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setting the view:
        int gameId = getIntent().getExtras().getInt(GamesInfoNames.GAME_ID);
        currentGame = GamesList.getInstance().getByID(gameId);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            case R.id.delete:
                // Delete confirmation
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_warning)
                        .setMessage(R.string.delete_confirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                GamesList.getInstance().remove(currentGame);
                                onBackPressed();
                            }})
                        .setNegativeButton(R.string.no, null).show();
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentGame.getPrettyText());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /* Allways use this method to create an intent,
    so you will know what parameters the intent needs */
    public static Intent createIntent(int gameID) {
        Intent intent = new Intent("com.juanma.drunkgames.activities.GameActivity");
        intent.putExtra(GamesInfoNames.GAME_ID, gameID);
        return intent;
    }

}

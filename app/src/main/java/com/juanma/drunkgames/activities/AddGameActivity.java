package com.juanma.drunkgames.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.juanma.drunkgames.Game;
import com.juanma.drunkgames.GamesList;
import com.juanma.drunkgames.R;


public class AddGameActivity extends ActionBarActivity {

    EditText description, instructions, addons, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        setTitle(R.string.newGame);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        description = (EditText) findViewById(R.id.descriptionInput);
        instructions = (EditText) findViewById(R.id.instructionsInput);
        addons = (EditText) findViewById(R.id.addonsInput);
        name = (EditText) findViewById(R.id.nameInput);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (instructions.getText().toString().equals("") ||
                        name.getText().toString().equals("")) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.cannotSave)
                            .setMessage(R.string.cannotSaveWarning)
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Game newGame = new Game();
                    newGame.setTitle(name.getText().toString());
                    newGame.setInstructions(instructions.getText().toString());
                    newGame.setDescription(description.getText().toString());
                    newGame.setAddons(addons.getText().toString());
                    GamesList.getInstance().add(newGame);
                    onBackPressed();
                }
                break;
            case R.id.cancel:
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public static Intent createIntent() {
        return new Intent("com.juanma.drunkgames.activities.AddGameActivity");
    }

}

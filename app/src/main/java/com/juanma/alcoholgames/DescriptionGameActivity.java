package com.juanma.alcoholgames;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.juanma.alcoholgames.fragments.DescriptionFragment;
import com.juanma.alcoholgames.utils.GamesInfoNames;

public class DescriptionGameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_game);

        // Create the DescriptionFragment:
        Game game = (Game) getIntent().getExtras().getSerializable(GamesInfoNames.GAME_OBJ);
        DescriptionFragment fragment = DescriptionFragment.newInstance(game);

        // Finally I add the DescriptionFragment to the view
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
    }

}

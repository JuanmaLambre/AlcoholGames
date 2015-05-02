package com.juanma.alcoholgames;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;
import com.juanma.alcoholgames.adapter.GameTabsPagerAdapter;
import com.juanma.alcoholgames.utils.GamesInfoNames;


public class GameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new GameTabsPagerAdapter(
                getSupportFragmentManager(),
                (Game) getIntent().getExtras().getSerializable(GamesInfoNames.GAME_OBJ)
        ));

        // Give the PagerSlidingTabStrip the ViewPager:
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

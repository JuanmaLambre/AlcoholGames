package com.juanma.alcoholgames.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.juanma.alcoholgames.Game;
import com.juanma.alcoholgames.fragments.AddonsFragment;
import com.juanma.alcoholgames.fragments.DescriptionFragment;


public class GameTabsPagerAdapter extends FragmentStatePagerAdapter {
    // To add a new title start by adding a string value to the array:
    private String tabTitles[] = new String[] { "Descripción", "Add-Ons" };
    private Game game;


    public GameTabsPagerAdapter(FragmentManager fm, Game actualGame) {
        super(fm);
        this.game = actualGame;
    }

    @Override
    public int getCount() {
        return this.tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return DescriptionFragment.newInstance(this.game);
            case 1:
                return AddonsFragment.newInstance(this.game);
        }

        // Should not receive an invalid position
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return this.tabTitles[position];
    }
}
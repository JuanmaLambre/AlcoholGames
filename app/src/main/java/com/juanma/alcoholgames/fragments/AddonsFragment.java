package com.juanma.alcoholgames.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juanma.alcoholgames.Game;
import com.juanma.alcoholgames.utils.GamesInfoNames;
import com.juanma.alcoholgames.R;
import android.support.v4.app.Fragment;
import android.widget.TextView;


public class AddonsFragment extends Fragment {

    public static AddonsFragment newInstance(Game actualGame) {
        AddonsFragment fragment = new AddonsFragment();
        Bundle args = new Bundle();
        args.putSerializable(GamesInfoNames.GAME_OBJ, actualGame);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.addons_fragment, container, false);

        // Setting the data:
        Game actualGame = (Game) getArguments().getSerializable(GamesInfoNames.GAME_OBJ);
        TextView textView = (TextView) view.findViewById(R.id.addonsText);
        textView.setText(actualGame.getAddons());

        return view;
    }
}

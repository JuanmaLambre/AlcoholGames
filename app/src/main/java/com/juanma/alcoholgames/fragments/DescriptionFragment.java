package com.juanma.alcoholgames.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juanma.alcoholgames.Game;
import com.juanma.alcoholgames.utils.GamesInfoNames;
import com.juanma.alcoholgames.R;
import com.juanma.alcoholgames.utils.Misc;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class DescriptionFragment extends Fragment {

    public static DescriptionFragment newInstance(Game game) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putSerializable(GamesInfoNames.GAME_OBJ, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.description_fragment, container, false);
        TextView descriptionText, instructionsText;
        descriptionText = (TextView) view.findViewById(R.id.descriptionText);
        instructionsText = (TextView) view.findViewById(R.id.instructionsText);

        // Setting the data
        Game actualGame = (Game) getArguments().getSerializable(GamesInfoNames.GAME_OBJ);
        descriptionText.setText(actualGame.getDescription());
        instructionsText.setText(actualGame.getInstructions());

        return view;
    }
}

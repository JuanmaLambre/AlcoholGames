package com.juanma.alcoholgames.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juanma.alcoholgames.Game;
import com.juanma.alcoholgames.R;

import java.util.List;

public class GameArrayAdapter extends ArrayAdapter<Game> {

    private LayoutInflater layoutInflater;

    public GameArrayAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.game_list_view, null);
        final Game game = (Game) getItem(position);

        // Setting the view:
        TextView gameName, subtitle;
        gameName = (TextView) view.findViewById(R.id.game_name);
        gameName.setText(game.getTitle());

        ImageView favIcon = (ImageView) view.findViewById(R.id.fav_icon);
        if ( !game.isFaved() ) {
            favIcon.setVisibility(View.GONE);
        } else {
            favIcon.setVisibility(View.VISIBLE);
        }

        return view;
    }
}

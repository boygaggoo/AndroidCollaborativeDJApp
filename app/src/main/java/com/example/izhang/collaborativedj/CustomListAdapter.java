package com.example.izhang.collaborativedj;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Set;


/**
 * Created by Corey on 6/17/2015.
 */
public class CustomListAdapter extends ArrayAdapter<SongItem> {

    private final ArrayList<SongItem>  songItems;

    private  String IMAGEPATH = "http://app.engagebyeview.com/portal/VideoThumbNails/";
    public CustomListAdapter(Activity context, ArrayList<SongItem> songItems) {
        super(context, R.layout.list_item, songItems);
        // TODO Auto-generated constructor stub

        this.songItems=songItems;
    }

    public View getView(final int position,View view,final ViewGroup parent) {
        View v = view;
        final Context viewContext = getContext();
        if (v == null) {
            Log.v("view", "view is null");
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, parent, false);
        }
        final SongItem songItem = songItems.get(position);
        TextView songTitle = (TextView) v.findViewById(R.id.songTitle);
        TextView songInfo = (TextView) v.findViewById(R.id.songInfo);
        ImageView upArrow = (ImageView) v.findViewById(R.id.upArrow);
        ImageView downArrow = (ImageView) v.findViewById(R.id.downArrow);



        songTitle.setText(songItems.get(position).getName());
        songInfo.setText(songItems.get(position).getArtist() + songItems.get(position).getAlbum());

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("up arrow", "up arrow clicked");

            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("down arrow", "down arrow clicked");
            }
        });

        return v;
    }

}
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Set;


/**
 * Created by Corey on 6/17/2015.
 */
public class CustomListAddAdapter extends ArrayAdapter<SongItem> {

    private final ArrayList<SongItem>  songItems;

    private  String IMAGEPATH = "http://app.engagebyeview.com/portal/VideoThumbNails/";
    public CustomListAddAdapter(Activity context, ArrayList<SongItem> songItems) {
        super(context, R.layout.listadd_item, songItems);
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
        ImageView addButton = (ImageView) v.findViewById(R.id.addButton);




        songTitle.setText(songItems.get(position).getName());
        songInfo.setText(songItems.get(position).getArtist() + songItems.get(position).getAlbum());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "";
// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.


                                Log.v("Server response:", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Could not connect to internet" + error.toString(),
                                Toast.LENGTH_LONG).show();



                    }
                });
                Log.v("add button", "up arrow clicked");

            }
        });



        return v;
    }

}
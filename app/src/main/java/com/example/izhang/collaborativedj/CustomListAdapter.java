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
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by Corey on 6/17/2015.
 */
public class CustomListAdapter extends ArrayAdapter<SongItem> {

    private final ArrayList<SongItem>  songItems;
    private String playlistID;

    private  String IMAGEPATH = "http://app.engagebyeview.com/portal/VideoThumbNails/";
    public CustomListAdapter(Activity context, ArrayList<SongItem> songItems, String playlistID) {
        super(context, R.layout.list_item, songItems);
        this.playlistID = playlistID;
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
        final ImageView upArrow = (ImageView) v.findViewById(R.id.upArrow);
        final ImageView downArrow = (ImageView) v.findViewById(R.id.downArrow);



        songTitle.setText(songItems.get(position).getName());
        songInfo.setText(songItems.get(position).getArtist() + songItems.get(position).getAlbum());

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongItem item = songItems.get(position);
                if (item.getVote() == 2) {
                    item.neutral();

                } else {
                    item.upvote();
                    //server call
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url ="http://collaborativedj.herokuapp.com/voteSong";
                    StringRequest request = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    try {
                                        Log.v("Upvote", response);
                                    } catch (Exception e){
                                        e.printStackTrace();

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Upvote failed", Toast.LENGTH_LONG).show();
                        }

                    }){
                        @Override
                        public HashMap<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("vote", "-1");
                            params.put("trackURI", songItem.getURI());
                            params.put("playlistID", playlistID);
                            return params;
                        }
                    };
                }
                setImages(item.getVote(), downArrow, upArrow);
                Log.v("up arrow", "up arrow clicked");
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongItem item = songItems.get(position);
                if (item.getVote() == 1) {
                    item.neutral();

                } else {
                    item.downvote();
                    //server call
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = "http://collaborativedj.herokuapp.com/voteSong";
                    StringRequest request = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    try {
                                        Log.v("Downvote", response);
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Downvote failed", Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        public HashMap<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("vote", "1");
                            params.put("trackURI", songItem.getURI());
                            params.put("playlistID", playlistID);
                            return params;
                        }
                    };
                }

                setImages(item.getVote(), downArrow,upArrow);
                Log.v("down arrow", "down arrow clicked");
            }
        });
        setImages(songItems.get(position).getVote(), downArrow, upArrow);
        return v;
    }
    public void setImages(int vote, ImageView downArrow, ImageView upArrow){
        if(vote == 0){
            downArrow.setImageResource(R.mipmap.ic_expand_more_black_24dp);
            upArrow.setImageResource(R.mipmap.ic_expand_less_black_24dp);
        }
        else if(vote ==1){
            upArrow.setImageResource(R.mipmap.ic_expand_less_black_24dp);
            downArrow.setImageResource(R.mipmap.ic_expand_more_blue_24dp);

        }
        else{
            downArrow.setImageResource(R.mipmap.ic_expand_more_black_24dp);
            upArrow.setImageResource(R.mipmap.ic_expand_less_pink_24dp);

        }

        return;
    }



}
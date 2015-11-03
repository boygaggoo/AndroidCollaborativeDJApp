package com.example.izhang.collaborativedj;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



import android.view.View;
import android.widget.Button;


import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thebeist on 10/2/2015.
 */
public class AddSong  extends AppCompatActivity {

    private boolean searchCompleted = false;
    private ArrayList<SongItem> returnedList = new ArrayList<>();
    private ListView songsDisplayed;
    private Activity activity;
    private String playlistID;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsong);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        playlistID = extras.getString("PlaylistID");
        Log.v("PLAYLIST", playlistID);
        activity = this;

        final EditText searchBox = (EditText) this.findViewById(R.id.searchBox);
        Button searchButton = (Button)this.findViewById(R.id.SearchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getSearchText = searchBox.getText().toString();
                Log.v("AddSong", "Playlist: " + playlistID);
                Log.v("AddSong", "Track: " + getSearchText);
                returnedList.clear();

                ArrayList<SongItem> queryList= new ArrayList<SongItem>();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "Http://collaborativedj.herokuapp.com/searchTrack";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("AddSong", response);
                                /*
                                songsDisplayed = (ListView) findViewById(R.id.listView2);
                                CustomListAddAdapter adapter=new CustomListAddAdapter(activity, returnedList, playlistID);
                                // Assign adapter to ListView
                                songsDisplayed.setAdapter(adapter);
                                */
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONObject jsonTrackObj = jsonObj.getJSONObject("tracks");
                                    JSONArray jsonSongList = jsonTrackObj.getJSONArray("items");

                                    if(jsonSongList.length() == 0){
                                        Toast.makeText(getApplicationContext(), "No Search Results", Toast.LENGTH_LONG).show();
                                    }
                                    for(int i = 0; i < jsonSongList.length(); i++){
                                        JSONArray artistArray = jsonSongList.getJSONObject(i).getJSONArray("artists");
                                        String artist = artistArray.getJSONObject(0).getString("name");
                                        returnedList.add(new SongItem(jsonSongList.getJSONObject(i).getString("name"), artist, "", jsonSongList.getJSONObject(i).getString("uri"), 0));
                                        Log.v("AddSong","Name: " + jsonSongList.getJSONObject(i).getString("name") + "URI: " + jsonSongList.getJSONObject(i).getString("uri") );
                                    }


                                    songsDisplayed  = (ListView) findViewById(R.id.listView2);
                                    CustomListAddAdapter adapter = new CustomListAddAdapter(activity, returnedList, playlistID);
                                    songsDisplayed.setAdapter(adapter);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("AddSong", "ERROR " + error.toString());
                        Toast.makeText(getApplicationContext(), "Could not connect to internet" + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("track", getSearchText);
                        params.put("playlistId", playlistID);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }

        });


        /*
        songsDisplayed  = (ListView) findViewById(R.id.listView2);
        returnedList.add(new SongItem("trap queen", "fetty wwap", "idk", null, 0));
        CustomListAddAdapter adapter = new CustomListAddAdapter(activity, returnedList, playlistID);
        songsDisplayed.setAdapter(adapter);
        */

        //should be able to search song from top and will display options in list.
        // once a song is added add to playlist by calling to server
        // TODO: This doesnt work, need to add button to the view
        /*
        Button createPlaylist = (Button)findViewById(R.id.createPlaylistButton);

        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }


}

package com.example.izhang.collaborativedj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Corey on 9/25/2015.
 *
 * Shows the playlist, allows people to vote
 */
public class Playlist extends AppCompatActivity {
    private ListView songList;
    private ArrayList<SongItem> songItems = new ArrayList<SongItem>();
    private ArrayList<SongItem> prevSongs;
    private Activity playlistActivity;
    private String playlistID = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistActivity = this;
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlaylist);

        SharedPreferences prefs = getSharedPreferences("PLAYLISTID", MODE_PRIVATE);
        playlistID = prefs.getString("playlistID", "");
        Log.v("Playlist", playlistID);

        toolbar.setTitle(playlistID);

        setSupportActionBar(toolbar);

        songList = (ListView) findViewById(R.id.listView);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://collaborativedj.herokuapp.com/playlist/" + playlistID;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        songItems.clear();
                        Log.v("Playlist", response);


                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray songObjArray = responseObj.getJSONArray("songs");

                                for (int i = 0; i < songObjArray.length(); i++) {
                                    JSONObject tempObj = songObjArray.getJSONObject(i);
                                    Log.v("Playlist", tempObj.toString());
                                    songItems.add(new SongItem(tempObj.getString("track_name"), " ", " ", tempObj.getString("song_uri"), tempObj.getInt("score")));
                                    if(prevSongs != null) {
                                        for (int j = 0; j < prevSongs.size(); j++) {
                                            if (prevSongs.get(j).getURI().equals(tempObj.getString("song_uri"))) {
                                                if (prevSongs.get(j).getVote() == 1) {
                                                    songItems.get(i).downvote();
                                                } else if (prevSongs.get(j).getVote() == 2) {
                                                    songItems.get(i).upvote();
                                                }
                                            }
                                        }
                                    }

                                }
                            prevSongs = new ArrayList<>();
                            for (int i = 0; i < songItems.size(); i++) {
                                prevSongs.add(songItems.get(i));
                            }

                            CustomListAdapter adapter = new CustomListAdapter(playlistActivity, songItems, playlistID);
                            // Assign adapter to ListView
                            songList.setAdapter(adapter);

                        } catch (Exception e){
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "This Playlist ID does not exist.", Toast.LENGTH_LONG).show();
            }

        });

        queue.add(request);

        Button addSong = (Button) findViewById(R.id.addSongButton);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddSong.class);
                i.putExtra("PlaylistID", playlistID);
                startActivity(i);
            }
        });

        //get information from server and add to songlist for display
        mSocket.on("playlist updated", playlistUpdated);
        mSocket.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://collaborativedj.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener playlistUpdated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            playlistActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.v("Socket.io", data.toString());

                    String track_name;
                    int score;
                    String song_uri;
                    try {
                        JSONArray array = data.getJSONArray("songs");
                        songItems.clear();
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject temp = array.getJSONObject(i);
                            track_name = temp.getString("track_name");
                            score = temp.getInt("score");
                            song_uri = temp.getString("song_uri");
                            songItems.add(new SongItem(track_name, " ", " ", song_uri, score));
                            if(prevSongs != null) {
                                for (int j = 0; j < prevSongs.size(); j++) {
                                    if (prevSongs.get(j).getURI().equals(temp.getString("song_uri"))) {
                                        if (prevSongs.get(j).getVote() == 1) {
                                            songItems.get(i).downvote();
                                        } else if (prevSongs.get(j).getVote() == 2) {
                                            songItems.get(i).upvote();
                                        }
                                    }
                                }
                            }

                            Log.v("Socket", track_name + " " + score);
                        }
                        prevSongs = new ArrayList<>();
                        for (int i = 0; i < songItems.size(); i++) {
                            prevSongs.add(songItems.get(i));
                        }
                        CustomListAdapter adapter = new CustomListAdapter(playlistActivity, songItems, playlistID);
                        // Assign adapter to ListView
                        songList.setAdapter(adapter);
                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };
}
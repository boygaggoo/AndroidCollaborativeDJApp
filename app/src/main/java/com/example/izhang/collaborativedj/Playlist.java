package com.example.izhang.collaborativedj;

import android.app.Activity;
import android.content.Intent;
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
    private Activity playlistActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistActivity = this;
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlaylist);
        Bundle extras = getIntent().getExtras();
        String playlistID = extras.getString("PlaylistID");
        Log.v("PLAYLIST", playlistID);
        toolbar.setTitle("Code:" + playlistID);
        setSupportActionBar(toolbar);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://collaborativedj.herokuapp.com/playlist/" + playlistID;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            Log.v("PLAYLIST!", response);
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
                i.putExtra("PlaylistID", "0aH0ytXyaOcl9eGxHwokUI");
                startActivity(i);
            }
        });

        //get information from server and add to songlist for display
        mSocket.on("new message", onNewMessage);
        mSocket.connect();
        songItems.add(new SongItem("trap queen", "fetty wap", "idk", null, 0));
        songList = (ListView) findViewById(R.id.listView);
        CustomListAdapter adapter=new CustomListAdapter(this, songItems, playlistID);
        // Assign adapter to ListView
        songList.setAdapter(adapter);
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
            mSocket = IO.socket("http://collaborativedj.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            playlistActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String songName;
                    String votes;
                    try {
                        songName = data.getString("songname");
                        votes = data.getString("votes");
                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };
}

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

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddSong);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        playlistID = extras.getString("PlaylistID");
        Log.v("PLAYLIST", playlistID);

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
        songsDisplayed = (ListView) findViewById(R.id.returnedSongs);
        returnedList.add(new SongItem("trap queen", "fetty wap", "idk", null, 0));
        CustomListAddAdapter adapter=new CustomListAddAdapter(activity, returnedList, playlistID);
        // Assign adapter to ListView
        songsDisplayed.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_addsong, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Do whatever you want
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                songsDisplayed = (ListView) findViewById(R.id.listView);
                CustomListAddAdapter adapter=new CustomListAddAdapter(activity, returnedList, playlistID);
                // Assign adapter to ListView
                songsDisplayed.setAdapter(adapter);
                return true;
            }
        });
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();


        searchView.setIconifiedByDefault(false);
        if(searchView == null)
            Log.v("searchview", "yo its null");
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        ArrayList<SongItem> queryList= new ArrayList<SongItem>();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            searchCompleted = true;
            //send string to server
                Log.v("query", query);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "Http://collaborativedj.herokuapp.com/searchTrack";
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //set returned list
                            songsDisplayed = (ListView) findViewById(R.id.listView);
                            CustomListAddAdapter adapter=new CustomListAddAdapter(activity, returnedList, playlistID);
                            // Assign adapter to ListView
                            songsDisplayed.setAdapter(adapter);
                            Log.v("Server response:", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Could not connect to internet" + error.toString(),
                            Toast.LENGTH_LONG).show();



                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("track", query);
                    params.put("playlistId", playlistID );

                    return params;
                }



               /* @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }*/
            };

// Add the request to the RequestQueue.
            queue.add(stringRequest);



            //update list with new results



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}

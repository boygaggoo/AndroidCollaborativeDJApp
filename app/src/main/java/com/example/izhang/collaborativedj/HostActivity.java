package com.example.izhang.collaborativedj;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Corey on 9/25/2015.
 *
 * HostActivity : Allows Host to create a playlist
 */
public class HostActivity extends AppCompatActivity {
    public String userID = "";
    public String accessToken = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHost);
        setSupportActionBar(toolbar);

        Bundle loginExtras = getIntent().getExtras();
        userID = loginExtras.get("user_id").toString();
        accessToken = loginExtras.get("access_token").toString();

        // Get the userID that will be sent as a bundle from login
        Button createPlaylist = (Button)findViewById(R.id.createPlaylistButton);
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call create playlist here.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://collaborativedj.herokuapp.com/createPlaylist/";
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsResponse = new JSONObject(response);
                                    Intent i = new Intent(getApplicationContext(), Playlist.class);
                                    String playlistID = jsResponse.getString("playlistId");

                                    SharedPreferences.Editor editor = getSharedPreferences("PLAYLISTID", MODE_PRIVATE).edit();
                                    editor.putString("playlistID", playlistID);
                                    editor.commit();

                                    i.putExtra("PlaylistID", playlistID);
                                    startActivity(i);

                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                                // Display the first 500 characters of the response string.
                                Log.v("PLAYLIST!", "Response" + response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("PLAYLIST!", error.toString());
                    }
                }){
                    @Override
                    protected HashMap<String, String> getParams() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("userId", userID);
                        headers.put("accessToken", accessToken);
                        return headers;
                    }
                };

                Log.v("PLAYLIST!", "UserID: " + userID);
                Log.v("PLAYLIST!", "AccessToken: " + accessToken);


                queue.add(request);

               /*
               Intent i = new Intent(getApplicationContext(), Playlist.class);
               i.putExtra("PlaylistID", "0aH0ytXyaOcl9eGxHwokUI");
               startActivity(i);
               */
            }
        });

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
}
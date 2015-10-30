package com.example.izhang.collaborativedj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Spotify;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

/*
Main Page - Login
 */
public class login extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "collabdj://spotifycallback";
    private static final String CLIENT_ID = "6fb90af0cf48488f866539ded0b7fab7";
    String userID = "";
    String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText code = (EditText)findViewById(R.id.codeEdit);

        Button joinButton = (Button)findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Playlist.class);
                i.putExtra("PlaylistID", "0aH0ytXyaOcl9eGxHwokUI");
                startActivity(i);
                //call server to check if code works
                if(checkCode(code.getText().toString())) {
                    i = new Intent(getApplicationContext(), Playlist.class);
                    i.putExtra("PlaylistID", "0aH0ytXyaOcl9eGxHwokUI");
                    startActivity(i);

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    //// TODO: 10/29/15 : Change URL to actual.
                    String url = "Http://";
                    //// // TODO: 10/29/15

                    JsonObjectRequest req = new JsonObjectRequest(url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.has("status")) {
                                            int statusCode = response.getInt("status");
                                            if(statusCode == 200) {
                                                Intent i = new Intent(getApplicationContext(), Playlist.class);
                                                startActivity(i);
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Wrong Playlist Code", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(req);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter a code",
                            Toast.LENGTH_LONG).show();
                    Log.v("Try Join", "code was null");
                }
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
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

    public static boolean checkCode(String code){
        if(code == null)
            return false;
        boolean result = false;
            if(code.length() > 0 && code.length() < 10)
                result = true;
        return result;
    }

    public void onSpotifyLogin(View view){
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private","streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    accessToken = response.getAccessToken();
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url ="https://api.spotify.com/v1/me/";
                    StringRequest request = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        userID = obj.getString("id");
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("TOKEN!", "Shit Failed");
                        }
                    }){
                        @Override
                        public HashMap<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", "Bearer " + response.getAccessToken());
                            return headers;
                        }
                    };

                    queue.add(request);

                    Log.v("TOKEN!", response.getAccessToken());
                    Toast.makeText(getApplicationContext(), "Access Token: " + response.getAccessToken() + "\nResult Type: " + response.getType().toString() + "\n Expires In: " + response.getExpiresIn(), Toast.LENGTH_LONG).show();
                    Intent host = new Intent(this, HostActivity.class);
                    host.putExtra("user_id", userID);
                    host.putExtra("access_token", accessToken);
                    startActivity(host);
                    break;
                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}

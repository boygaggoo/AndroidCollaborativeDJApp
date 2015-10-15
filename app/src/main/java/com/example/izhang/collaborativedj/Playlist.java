package com.example.izhang.collaborativedj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Corey on 9/25/2015.
 */
public class Playlist extends AppCompatActivity {
    private ListView songList;
    private ArrayList<SongItem> songItems = new ArrayList<SongItem>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlaylist);
        setSupportActionBar(toolbar);

        Button addSong = (Button) findViewById(R.id.addSongButton);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddSong.class);
                startActivity(i);
            }
        });

        //get information from server and add to songlist for display
        songItems.add(new SongItem("trap queen", "fetty wap", "idk"));
        songList = (ListView) findViewById(R.id.listView);
        CustomListAdapter adapter=new CustomListAdapter(this, songItems);
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
}

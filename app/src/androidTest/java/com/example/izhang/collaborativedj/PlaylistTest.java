package com.example.izhang.collaborativedj;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.izhang.collaborativedj.HostActivity;
import com.example.izhang.collaborativedj.Playlist;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class PlaylistTest
        extends ActivityUnitTestCase<Playlist> {

    private Intent activityIntent;
    private Activity mPlaylistTest;
    private Listview songList;
    private ArrayList<SongItem> songItems;

    public PlaylistTest() {
        super(Playlist);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activityIntent = new Intent(getInstrumentation()
                .getTargetContext(), Playlist);
        startActivity(activityIntent, null, null);
        mPlaylistTest = getActivity();
    }
    @LargeTest
    public void testListview() {
        startActivity(activityIntent, null, null);


    }

}
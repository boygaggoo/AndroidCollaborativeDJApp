package com.example.izhang.collaborativedj;

/**
 * Created by thebeist on 10/2/2015.
 */
public class SongItem {
    private String name;
    private String artist;
    private String album;

    public SongItem() {
        name = null;
        artist = null;
        album = null;
    }

    public SongItem(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }
}

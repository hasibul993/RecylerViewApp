package com.recyclerviewapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BookMEds on 20-04-2018.
 */

public class Model {

    @SerializedName("Name")
    private String SongName;
    @SerializedName("Artist")
    private String Artist;
    @SerializedName("Album")
    private String Album;

    public void Model(String SongName,String Artist,String Album){
        this.SongName=SongName;
        this.Artist=Artist;
        this.Album=Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getAlbum() {
        return Album;
    }

    public String getArtist() {
        return Artist;
    }

    public String getSongName() {
        return SongName;
    }
}

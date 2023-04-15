package com.example.playlist;

public class Music {
    private int id;
    private String name;
    private String singer;
    private String album;

    public Music(int id, String name, String singer, String album) {
        this.id = id;
        this.name = name;
        this.singer = singer;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}

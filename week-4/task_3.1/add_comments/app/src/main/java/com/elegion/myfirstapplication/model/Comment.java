package com.elegion.myfirstapplication.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.elegion.myfirstapplication.model.Album;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity(tableName = "comment",
        foreignKeys = @ForeignKey(entity = Album.class, parentColumns = "id", childColumns = "album_id"),
        indices = @Index(value = "album_id"))
public class Comment {
    @PrimaryKey
    private int id;

    @SerializedName("album_id")
    @ColumnInfo(name = "album_id")
    private int albumId;

    private String text;

    private String author;

    private Date timestamp;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
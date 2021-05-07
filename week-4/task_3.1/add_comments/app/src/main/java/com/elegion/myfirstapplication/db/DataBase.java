package com.elegion.myfirstapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.AlbumSong;
import com.elegion.myfirstapplication.model.Comment;
import com.elegion.myfirstapplication.model.Song;
import com.elegion.myfirstapplication.model.converter.ConverterDB;

@Database(entities = {Album.class, Song.class, AlbumSong.class, Comment.class}, version = 2, exportSchema = false)
@TypeConverters(ConverterDB.class)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}

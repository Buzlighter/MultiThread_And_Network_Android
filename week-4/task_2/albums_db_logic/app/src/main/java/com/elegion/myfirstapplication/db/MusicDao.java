package com.elegion.myfirstapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.AlbumSong;
import com.elegion.myfirstapplication.model.Song;

import java.util.List;

/**
 * @author Azret Magometov
 */

@Dao
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinkAlbumSong(AlbumSong albumSong);

    @Query("SELECT * FROM album")
    List<Album> getAlbums();

    @Query("SELECT * FROM song")
    List<Song> getSongs();

    @Query("select * from album where id = :albumId")
    Album getAlbumById(int albumId);

    @Query("SELECT * FROM song INNER JOIN albumsong ON song.id = albumsong.song_id WHERE album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

    //удалить альбом
    @Delete
    void deleteAlbum(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album WHERE id = :albumId")
    void deleteAlbumById(int albumId);



}

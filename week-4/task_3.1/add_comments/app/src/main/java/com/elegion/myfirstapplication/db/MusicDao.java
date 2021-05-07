package com.elegion.myfirstapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.AlbumSong;
import com.elegion.myfirstapplication.model.Comment;
import com.elegion.myfirstapplication.model.Song;

import java.util.List;

/**
 * @author Azret Magometov
 */

@Dao
public abstract class MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void setLinkAlbumSong(AlbumSong albumSong);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAlbum(Album album);

    @Query("SELECT * FROM album")
    public abstract List<Album> getAlbums();

    @Query("SELECT * FROM song")
    public abstract List<Song> getSongs();

    @Query("select * from album where id = :albumId")
    public abstract Album getAlbumById(int albumId);

    @Query("SELECT * FROM song INNER JOIN albumsong ON song.id = albumsong.song_id WHERE album_id = :albumId")
    public abstract List<Song> getSongsFromAlbum(int albumId);

    //удалить альбом
    @Delete
    public abstract void deleteAlbum(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album WHERE id = :albumId")
    public abstract void deleteAlbumById(int albumId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertComments(List<Comment> comments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertComment(Comment comment);

    @Query("SELECT * from comment where id = :commentId")
    public abstract Comment getComment(int commentId);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAlbumComments(Album album) {
        insertAlbum(album);
        insertComments(album.getComments());
    }

    @Transaction
    public Album getAlbumByIdComments(int albumId) {
        Album album = getAlbumById(albumId);
        album.setComments(getCommentsByAlbumId(albumId));
        return album;
    }

    @Query("SELECT comment.* from comment inner join album on comment.album_id = album.id where album.id = :albumId")
    public abstract List<Comment> getCommentsByAlbumId(int albumId);


}

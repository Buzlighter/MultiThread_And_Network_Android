package com.elegion.myfirstapplication;

import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.Comment;
import com.elegion.myfirstapplication.model.Song;
import com.elegion.myfirstapplication.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by marat.taychinov
 */

public interface AcademyApi {

    @POST("registration")
    Completable registration(@Body User user);

    @GET("albums")
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Single<List<Song>> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);

    @GET("user")
    Single<User> authUser();

    @POST("comments")
    Completable addComment(@Body Comment comment);

    @GET("comments")
    Single<List<Comment>> getComments();

    @GET("albums/{id}/comments")
    Single<List<Comment>> getCommentsByAlbumId(@Path("id") int albumId);
}

package com.elegion.myfirstapplication;

import com.elegion.myfirstapplication.model.Album;
import com.elegion.myfirstapplication.model.Albums;
import com.elegion.myfirstapplication.model.Song;
import com.elegion.myfirstapplication.model.Songs;
import com.elegion.myfirstapplication.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by marat.taychinov
 */

public interface AcademyApi {
    @POST("registration")
    Call<User> registration(@Body User user);

    @GET("albums")
    Call<Albums> getAlbums();

    @GET("albums/{id}")
    Call<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Call<Songs> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);
}

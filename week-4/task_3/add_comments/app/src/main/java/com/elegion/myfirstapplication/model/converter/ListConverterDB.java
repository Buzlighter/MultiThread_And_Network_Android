package com.elegion.myfirstapplication.model.converter;

import android.arch.persistence.room.TypeConverter;

import com.elegion.myfirstapplication.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ListConverterDB {

    public static Gson gson = new Gson();

    @TypeConverter
    public static List<Song> stringToSongList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Song>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<Song> list) {
        return gson.toJson(list);

    }
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

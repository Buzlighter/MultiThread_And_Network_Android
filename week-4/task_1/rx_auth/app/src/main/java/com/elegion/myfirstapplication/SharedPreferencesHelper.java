package com.elegion.myfirstapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.elegion.myfirstapplication.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String USERS_KEY = "USERS_KEY";
    public static final Type USERS_TYPE = new TypeToken<List<User>>() {
    }.getType();


    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();

    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public List<User> getUsers() {
        List<User> users = mGson.fromJson(mSharedPreferences.getString(USERS_KEY, ""), USERS_TYPE);
        return users == null ? new ArrayList<User>() : users;
    }

    public boolean addUser(User user) {
        List<User> users = getUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return false;
            }
        }
        users.add(user);
        mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
        return true;
    }

    public boolean saveOrOverrideUser(User user) {
        List<User> users = getUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                users.remove(u);
                break;
            }
        }
        users.add(user);
        mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
        return true;
    }

    public List<String> getSuccessEmails() {
        List<String> successEmails = new ArrayList<>();
        List<User> allUsers = getUsers();
        for (User user : allUsers) {
            if (user.hasSuccessEmail()) {
                successEmails.add(user.getEmail());
            }
        }
        return successEmails;
    }

    public User login(String email, String password) {
        List<User> users = getUsers();
        for (User u : users) {
            if (email.equalsIgnoreCase(u.getEmail())
                    && password.equals(u.getPassword())) {
                u.setHasSuccessEmail(true);
                mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
                return u;
            }
        }
        return null;
    }
}

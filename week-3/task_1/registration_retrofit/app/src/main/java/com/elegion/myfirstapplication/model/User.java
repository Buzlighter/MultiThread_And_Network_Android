package com.elegion.myfirstapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private String mPassword;

    private boolean mHasSuccessEmail;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public User(String email, String name, String password) {
        mEmail = email;
        mName = name;
        mPassword = password;
    }

    public static class DataBean implements Serializable{
        private int id;
        private String name;
        private String email;

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }
    }

    public String getEmail() {
        return mEmail == null ? data.getEmail() : mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName == null ? data.getName() : mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean hasSuccessEmail() {
        return mHasSuccessEmail;
    }

    public void setHasSuccessEmail(boolean hasSuccessEmail) {
        mHasSuccessEmail = hasSuccessEmail;
    }


}
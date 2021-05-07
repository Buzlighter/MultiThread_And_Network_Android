package com.elegion.myfirstapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mName;
    @SerializedName("password")
    private String mPassword;

    private boolean mHasSuccessEmail;


    @SerializedName("errors")
    private User.ErrorsDTO errors;

    public User.ErrorsDTO getErrors() {
        return errors;
    }

    public User(ErrorsDTO errors) {
        this.errors = errors;
    }

    public static class ErrorsDTO implements Serializable {
        @SerializedName("email")
        private List<String> email;
        @SerializedName("name")
        private List<String> name;
        @SerializedName("password")
        private List<String> password;

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<String> getPassword() {
            return password;
        }

        public void setPassword(List<String> password) {
            this.password = password;
        }
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

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
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

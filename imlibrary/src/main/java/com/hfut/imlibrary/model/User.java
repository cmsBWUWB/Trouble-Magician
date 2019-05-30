package com.hfut.imlibrary.model;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject {
    private String userId;
    private String username;
    private String picPath;
    private String password;

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof User && TextUtils.equals(((User) obj).userId, userId);
    }

    public String getUsername() {
        return username;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("userId = %s,userName = %s,picPath = %s", userId, username, picPath);
    }
}

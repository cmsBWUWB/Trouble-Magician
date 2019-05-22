package com.hfut.imlibrary.model;


import android.text.TextUtils;

public class User {
    private String userId;
    private String username;

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj) {
        return obj instanceof User && TextUtils.equals(((User) obj).userId, userId);
    }

    public String getUsername() {
        return username;
    }
}

package com.hfut.imlibrary.model;

import android.text.TextUtils;

public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User)){
            return false;
        }
        return TextUtils.equals(this.username, ((User) obj).username);
    }
}

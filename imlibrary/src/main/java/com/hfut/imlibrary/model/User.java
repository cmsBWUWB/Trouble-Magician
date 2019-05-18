package com.hfut.imlibrary.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public static List<User> usernameList2UserList(List<String> userNameList){
        List<User> userList = new ArrayList<>();
        for(String userName:userNameList){
            userList.add(new User(userName));
        }
        return userList;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User)){
            return false;
        }
        return TextUtils.equals(this.username, ((User) obj).username);
    }
}

package com.hfut.database.entity;

import java.util.List;

public class User {
    private String username;
    private List<Group> ownerGroupList;
    private List<Group> memberGroupList;
    private List<Chat> chatList;

    public User(String username, List<Group> ownerGroupList, List<Group> memberGroupList, List<Chat> chatList) {
        this.username = username;
        this.ownerGroupList = ownerGroupList;
        this.memberGroupList = memberGroupList;
        this.chatList = chatList;
    }

    public String getUsername() {
        return username;
    }

    public List<Group> getOwnerGroupList() {
        return ownerGroupList;
    }

    public List<Group> getMemberGroupList() {
        return memberGroupList;
    }

    public List<Chat> getChatList() {
        return chatList;
    }
}

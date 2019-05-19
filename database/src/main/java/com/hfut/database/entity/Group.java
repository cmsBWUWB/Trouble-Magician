package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private String ownerUsername;
    private List<User> members;

    public Group(String groupId, String groupName, String ownerUsername, List<User> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerUsername = ownerUsername;
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public List<User> getMembers() {
        return members;
    }
}

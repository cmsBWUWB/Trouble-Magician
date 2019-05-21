package com.hfut.imlibrary.model;

import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private String ownerUserId;
    private List<User> members;

    public Group(String groupId, String groupName, String ownerUserId, List<User> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerUserId = ownerUserId;
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public List<User> getMembers() {
        return members;
    }
}

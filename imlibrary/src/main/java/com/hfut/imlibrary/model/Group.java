package com.hfut.imlibrary.model;

public class Group {
    private String groupId;
    private String groupName;
    private String ownerUsername;

    public Group(String groupId, String groupName, String ownerUsername) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerUsername = ownerUsername;
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
}

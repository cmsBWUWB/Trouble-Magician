package com.hfut.imlibrary.model;

public class Group {
    private String groupId;
    private String groupName;
    private String ownerUserId;

    public Group(String groupId, String groupName, String ownerUserId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerUserId = ownerUserId;
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
}

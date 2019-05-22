package com.hfut.imlibrary.database;

public class GroupMember {
    private String groupId;
    private String userId;

    public GroupMember(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }
}

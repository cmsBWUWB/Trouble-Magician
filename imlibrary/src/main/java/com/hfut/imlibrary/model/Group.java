package com.hfut.imlibrary.model;

import android.text.TextUtils;

import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupId;
    private String groupName;
    private User owner;
    private List<User> members;

    public Group(String groupId, String groupName, User owner, List<User> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.owner = owner;
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public User getOwner() {
        return owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public static Group emGroup2Group(EMGroup emGroup) {
        String groupId = emGroup.getGroupId();
        String groupName = emGroup.getGroupName();
        User owner = new User(emGroup.getOwner());
        List<User> memberList = new ArrayList<>();
        for (String username : emGroup.getMembers()) {
            memberList.add(new User(username));
        }
        return new Group(groupId, groupName, owner, memberList);
    }

    public static List<Group> emGroupList2GroupList(List<EMGroup> emGroupList) {
        List<Group> groupList = new ArrayList<>();
        for (EMGroup emGroup : emGroupList) {
            groupList.add(emGroup2Group(emGroup));
        }
        return groupList;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        }
        return TextUtils.equals(this.groupId, ((Group) obj).groupId);
    }

}

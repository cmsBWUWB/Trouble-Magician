package com.hfut.imlibrary.listener;

import com.hyphenate.EMGroupChangeListener;

import java.util.List;

/**
 * Created by wzt on 2019/5/22
 */
public class BaseGroupChangeListener implements EMGroupChangeListener {
    private String groupId;

    public BaseGroupChangeListener() {
    }

    public BaseGroupChangeListener(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

    }

    @Override
    public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {

    }

    @Override
    public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

    }

    @Override
    public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {

    }

    @Override
    public void onInvitationAccepted(String groupId, String invitee, String reason) {

    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {

    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {

    }

    @Override
    public void onGroupDestroyed(String groupId, String groupName) {

    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

    }

    @Override
    public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {

    }

    @Override
    public void onMuteListRemoved(String groupId, List<String> mutes) {

    }

    @Override
    public void onAdminAdded(String groupId, String administrator) {

    }

    @Override
    public void onAdminRemoved(String groupId, String administrator) {

    }

    @Override
    public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {

    }
}

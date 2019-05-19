package com.hfut.imlibrary.listener;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupChangeListener implements EMGroupChangeListener {
    private IMListener imListener;

    public GroupChangeListener(IMListener imListener) {
        this.imListener = imListener;
    }

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
        //目前来讲，自动同意拉群邀请
        try {
            EMClient.getInstance().groupManager().acceptInvitation(groupId, inviter);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {
        //目前来讲，自动同意加群申请
        try {
            EMClient.getInstance().groupManager().acceptApplication(applicant, groupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
        imListener.groupChanged();
    }

    @Override
    public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
    }

    @Override
    public void onInvitationAccepted(String groupId, String invitee, String reason) {
        imListener.groupMemberChanged();
    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {

    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {
        imListener.groupChanged();
    }

    @Override
    public void onGroupDestroyed(String groupId, String groupName) {
        imListener.groupChanged();
    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
        imListener.groupChanged();
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
package com.hfut.imlibrary.listener;

import android.text.TextUtils;

import com.hfut.imlibrary.event.GroupChangeEvent;
import com.hfut.utils.utils.log.LogPrint;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GroupChangeListener implements EMGroupChangeListener {
    private List<BaseGroupChangeListener> listeners = new ArrayList<>();

    public void addListener(BaseGroupChangeListener baseGroupChangelistener) {
        listeners.add(baseGroupChangelistener);
    }

    public void removeListener(BaseGroupChangeListener baseGroupChangelistener) {
        listeners.remove(baseGroupChangelistener);
    }

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
        LogPrint.i("groupId = " + groupId);
        //目前来讲，自动同意拉群邀请
        try {
            EMClient.getInstance().groupManager().acceptInvitation(groupId, inviter);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onInvitationReceived(groupId, groupName, inviter, reason);
        }
    }

    @Override
    public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {
        LogPrint.i("groupId = " + groupId);
        //目前来讲，自动同意加群申请
        try {
            EMClient.getInstance().groupManager().acceptApplication(applicant, groupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onRequestToJoinReceived(groupId, groupName, applicant, reason);
        }
    }

    @Override
    public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
        LogPrint.i("groupId = " + groupId);
        EventBus.getDefault().post(new GroupChangeEvent());
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onRequestToJoinAccepted(groupId, groupName, accepter);
        }
    }

    @Override
    public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onRequestToJoinDeclined(groupId, groupName, decliner,reason);
        }
    }

    @Override
    public void onInvitationAccepted(String groupId, String invitee, String reason) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onInvitationAccepted(groupId, invitee, reason);
        }
    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onInvitationDeclined(groupId, invitee, reason);
        }
    }

    @Override
    public void onUserRemoved(String groupId, String groupName) {
        LogPrint.i("groupId = " + groupId);
        EventBus.getDefault().post(new GroupChangeEvent());
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onUserRemoved(groupId, groupName);
        }
    }

    @Override
    public void onGroupDestroyed(String groupId, String groupName) {
        LogPrint.i("groupId = " + groupId);
        EventBus.getDefault().post(new GroupChangeEvent());
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onGroupDestroyed(groupId, groupName);
        }
    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onAutoAcceptInvitationFromGroup(groupId, inviter, inviteMessage);
        }
    }

    @Override
    public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onMuteListAdded(groupId, mutes, muteExpire);
        }
    }

    @Override
    public void onMuteListRemoved(String groupId, List<String> mutes) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onMuteListRemoved(groupId, mutes);
        }
    }

    @Override
    public void onAdminAdded(String groupId, String administrator) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onAdminAdded(groupId, administrator);
        }
    }

    @Override
    public void onAdminRemoved(String groupId, String administrator) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onAdminRemoved(groupId, administrator);
        }
    }

    @Override
    public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onOwnerChanged(groupId, newOwner, oldOwner);
        }
    }

    @Override
    public void onMemberJoined(String groupId, String member) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onMemberJoined(groupId, member);
        }
    }

    @Override
    public void onMemberExited(String groupId, String member) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onMemberExited(groupId, member);
        }
    }

    @Override
    public void onAnnouncementChanged(String groupId, String announcement) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onAnnouncementChanged(groupId, announcement);
        }
    }

    @Override
    public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onSharedFileAdded(groupId, sharedFile);
        }
    }

    @Override
    public void onSharedFileDeleted(String groupId, String fileId) {
        LogPrint.i("groupId = " + groupId);
        for (BaseGroupChangeListener emGroupChangeListener : listeners) {
            if (!TextUtils.equals(emGroupChangeListener.getGroupId(),groupId)) continue;
            emGroupChangeListener.onSharedFileDeleted(groupId, fileId);
        }
    }
}

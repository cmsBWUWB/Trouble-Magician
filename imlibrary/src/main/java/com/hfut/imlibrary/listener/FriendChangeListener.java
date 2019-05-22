package com.hfut.imlibrary.listener;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.event.FriendChangeEvent;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

public class FriendChangeListener implements EMContactListener {
    @Override
    public void onContactAdded(String username) {
        IMManager.getInstance().getFriendListFromNet();
        EventBus.getDefault().post(new FriendChangeEvent());
    }

    @Override
    public void onContactDeleted(String username) {
        IMManager.getInstance().getFriendListFromNet();
        EventBus.getDefault().post(new FriendChangeEvent());
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //目前来讲自动同意好友申请
        try {
            EMClient.getInstance().contactManager().acceptInvitation(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFriendRequestAccepted(String username) {
    }

    @Override
    public void onFriendRequestDeclined(String username) {
    }
}

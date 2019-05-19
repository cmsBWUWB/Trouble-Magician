package com.hfut.imlibrary.listener;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class FriendChangeListener implements EMContactListener {
    private IMListener imListener;

    public FriendChangeListener(IMListener imListener) {
        this.imListener = imListener;
    }

    @Override
    public void onContactAdded(String username) {
        imListener.friendChanged();
    }

    @Override
    public void onContactDeleted(String username) {
        imListener.friendChanged();
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
        imListener.friendChanged();
    }

    @Override
    public void onFriendRequestDeclined(String username) {

    }
}

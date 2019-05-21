package com.hfut.imlibrary.listener;

import com.hfut.imlibrary.event.FriendChangeEvent;
import com.hfut.imlibrary.model.User;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FriendChangeListener implements EMContactListener {
    private List<User> friendList;
    public FriendChangeListener(List<User> friendList){
        this.friendList = friendList;
    }

    @Override
    public void onContactAdded(String username) {
        friendList.add(new User(username));
        EventBus.getDefault().post(new FriendChangeEvent());
    }

    @Override
    public void onContactDeleted(String username) {
        friendList.remove(new User(username));
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
        EventBus.getDefault().post(new FriendChangeEvent());
    }

    @Override
    public void onFriendRequestDeclined(String username) {
    }
}

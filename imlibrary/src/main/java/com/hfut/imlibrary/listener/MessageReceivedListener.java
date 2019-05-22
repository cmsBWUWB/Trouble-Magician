package com.hfut.imlibrary.listener;

import android.util.Log;

import com.hfut.imlibrary.IMUtils;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MessageReceivedListener implements EMMessageListener {
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        EMClient.getInstance().chatManager().importMessages(messages);
        MessageReceivedEvent event = new MessageReceivedEvent(IMUtils.emMessageList2MessageList(messages));
        EventBus.getDefault().post(event);
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }
}

package com.hfut.imlibrary.listener;

import com.hfut.imlibrary.IMUtils;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.model.Message;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MessageReceivedListener implements EMMessageListener {
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for(EMMessage emMessage:messages){
            EMClient.getInstance().chatManager().saveMessage(emMessage);
        }
        EventBus.getDefault().post(new MessageReceivedEvent());
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

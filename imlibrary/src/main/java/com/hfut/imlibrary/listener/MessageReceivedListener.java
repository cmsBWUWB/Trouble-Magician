package com.hfut.imlibrary.listener;

import com.hfut.imlibrary.model.Message;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import static com.hfut.imlibrary.IMManager.emMessage2Message;

public class MessageReceivedListener implements EMMessageListener {
    private IMListener imListener;

    public MessageReceivedListener(IMListener imListener) {
        this.imListener = imListener;
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        List<Message> messageList = new ArrayList<>();
        for(EMMessage emMessage:messages){
            messageList.add(emMessage2Message(emMessage));
        }
        imListener.messageReceived(messageList);
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
    public void onMessageChanged(EMMessage message, Object change) {

    }
}

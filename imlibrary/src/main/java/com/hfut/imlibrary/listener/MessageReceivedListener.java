package com.hfut.imlibrary.listener;

import com.hfut.imlibrary.IMUtils;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.model.Message;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MessageReceivedListener implements EMMessageListener {
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        List<Message> messageList = new ArrayList<>();
        for(EMMessage emMessage:messages){
            messageList.add(IMUtils.emMessage2Message(emMessage));
        }
        // todo 将消息放入对应的会话当中
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
    public void onMessageChanged(EMMessage message, Object change) {

    }
}

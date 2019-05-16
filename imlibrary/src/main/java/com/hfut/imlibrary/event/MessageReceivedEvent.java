package com.hfut.imlibrary.event;

import com.hyphenate.chat.EMMessage;

import java.util.List;

public class MessageReceivedEvent{
    private List<EMMessage> emMessageList;
    public MessageReceivedEvent(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    public List<EMMessage> getEmMessageList() {
        return emMessageList;
    }
}

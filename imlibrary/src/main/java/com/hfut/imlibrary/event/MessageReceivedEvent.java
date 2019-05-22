package com.hfut.imlibrary.event;

import com.hfut.imlibrary.model.Message;

import java.util.List;

public class MessageReceivedEvent {
    private List<Message> newMessageList;

    public MessageReceivedEvent(List<Message> newMessageList) {
        this.newMessageList = newMessageList;
    }

    public List<Message> getNewMessageList() {
        return newMessageList;
    }
}

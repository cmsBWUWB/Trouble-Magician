package com.hfut.imlibrary.event;

import com.hfut.imlibrary.model.Message;

import java.util.List;

public class MessageReceivedEvent{
    private List<Message> emMessageList;
    public MessageReceivedEvent(List<Message> emMessageList) {
        this.emMessageList = emMessageList;
    }

    public List<Message> getEmMessageList() {
        return emMessageList;
    }
}

package com.hfut.imlibrary.model;

import java.util.List;

public class Chat {
    public enum ChatType {GROUP, FRIEND}
    private ChatType chatType;
    private List<Message> messageList;

    public Chat(ChatType chatType, List<Message> messageList) {
        this.chatType = chatType;
        this.messageList = messageList;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}

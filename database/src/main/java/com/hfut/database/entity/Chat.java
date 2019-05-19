package com.hfut.database.entity;

import java.util.List;

public class Chat {
    public enum ChatType {GROUP, FRIEND}

    private String chatId;
    private String username;
    private ChatType type;
    private List<Message> messageList;

    public Chat(String chatId, String username, ChatType type, List<Message> messageList) {
        this.chatId = chatId;
        this.username = username;
        this.type = type;
        this.messageList = messageList;
    }

    public String getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }

    public ChatType getType() {
        return type;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}

package com.hfut.imlibrary.model;

import java.util.List;

public class Chat {
    public enum ChatType {GROUP, FRIEND}
    private ChatType chatType;
    private String target;//好友id或者群名称
    private String lastMessage;
    private List<Message> messageList;

    public Chat(ChatType chatType, String target, String lastMessage, List<Message> messageList) {
        this.chatType = chatType;
        this.target = target;
        this.lastMessage = lastMessage;
        this.messageList = messageList;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public String getTarget() {
        return target;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}

package com.hfut.imlibrary.model;

public class Chat {
    private Message.MessageType messageType;
    private String targetId;//好友id或者群id
    private String targetName;//好友名或者群名
    private String lastMessage;//最后一条消息

    public Chat(Message.MessageType messageType, String target, String targetName, String lastMessage) {
        this.messageType = messageType;
        this.targetId = target;
        this.targetName = targetName;
        this.lastMessage = lastMessage;
    }

    public Message.MessageType getMessageType() {
        return messageType;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}

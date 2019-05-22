package com.hfut.imlibrary.model;

public class Message {
    public enum MessageType {FRIEND, GROUP}

    private String authorId;
    private MessageType type;
    private String chatId;//该消息所在的会话id
    private String content;
    private long time;

    public Message(String authorId, MessageType type, String chatId, String content, long time) {
        this.authorId = authorId;
        this.type = type;
        this.chatId = chatId;
        this.content = content;
        this.time = time;
    }

    public String getAuthorId() {
        return authorId;
    }

    public MessageType getType() {
        return type;
    }

    public String getChatId() {
        return chatId;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }
}

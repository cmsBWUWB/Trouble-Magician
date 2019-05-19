package com.hfut.database.entity;

public class Message {
    private long id;
    private String chatId;
    private String username;
    private long time;
    private String content;
    private String target;

    public Message(String chatId, String content, long time, String username, String target) {
        this.chatId = chatId;
        this.content = content;
        this.time = time;
        this.username = username;
        this.target = target;
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

    public String getUsername() {
        return username;
    }

    public String getTarget() {
        return target;
    }

}

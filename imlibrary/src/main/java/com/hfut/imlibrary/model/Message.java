package com.hfut.imlibrary.model;

public class Message {
    public enum MessageType {FRIEND, GROUP}

    private String authorId;
    private MessageType type;
    private String targetId;//该消息的目的地是哪里？
    private String content;
    private long time;

    public Message(String authorId, MessageType type, String targetId, String content, long time) {
        this.authorId = authorId;
        this.type = type;
        this.targetId = targetId;
        this.content = content;
        this.time = time;
    }

    public String getAuthorId() {
        return authorId;
    }

    public MessageType getType() {
        return type;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }
}

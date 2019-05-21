package com.hfut.imlibrary.model;

public class Message {
    private String authorId;
    private String content;
    private long time;

    public Message(String authorId, String content, long time) {
        this.authorId = authorId;
        this.content = content;
        this.time = time;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }
}

package com.hfut.imlibrary.model;

public class Message {
    private String username;
    private String content;
    private long time;
    private String target;

    public Message(String username, String content, long time, String target) {
        this.username = username;
        this.content = content;
        this.time = time;
        this.target = target;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public String getTarget() {
        return target;
    }
}

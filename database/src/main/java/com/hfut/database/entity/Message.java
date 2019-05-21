package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.*;

@Entity
public class Message {
    @Id
    private Long id;
    @NotNull
    private Long authorId;
    @NotNull
    private String content;
    @NotNull
    private long time;
    @NotNull
    private Long chatId;
    @Generated(hash = 1610049851)
    public Message(Long id, @NotNull Long authorId, @NotNull String content,
            long time, @NotNull Long chatId) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.time = time;
        this.chatId = chatId;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public Long getChatId() {
        return this.chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}

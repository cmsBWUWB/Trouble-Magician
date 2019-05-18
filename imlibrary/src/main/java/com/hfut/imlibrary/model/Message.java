package com.hfut.imlibrary.model;

import com.hyphenate.chat.EMMessage;

public class Message {
    private String content;
    private User sendUser;

    enum MessageType {GROUP, FRIEND}

    private MessageType type;
    private String target;

    public Message(String content, User sendUser, MessageType type, String target) {
        this.content = content;
        this.sendUser = sendUser;
        this.type = type;
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public User getSendUser() {
        return sendUser;
    }

    public MessageType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public static Message emMessage2Message(EMMessage emMessage) {
        return new Message(emMessage.getBody().toString(),
                new User(emMessage.getFrom()),
                emMessage.getChatType() == EMMessage.ChatType.Chat ? MessageType.FRIEND : MessageType.GROUP,
                emMessage.getTo());
    }
}

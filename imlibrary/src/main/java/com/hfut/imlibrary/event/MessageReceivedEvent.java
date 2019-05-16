package com.hfut.imlibrary.event;

public class MessageReceivedEvent extends BaseEvent{
    public MessageReceivedEvent(boolean success) {
        super(success);
    }
}

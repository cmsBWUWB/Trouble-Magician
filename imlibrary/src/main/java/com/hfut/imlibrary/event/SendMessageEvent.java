package com.hfut.imlibrary.event;

public class SendMessageEvent extends BaseEvent {
    public SendMessageEvent(boolean success) {
        super(success);
    }
}

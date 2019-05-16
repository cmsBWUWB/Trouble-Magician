package com.hfut.imlibrary.event;

public class LogoutEvent extends BaseEvent {
    public LogoutEvent(boolean success) {
        super(success);
    }
}

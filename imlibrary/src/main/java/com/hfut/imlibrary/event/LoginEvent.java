package com.hfut.imlibrary.event;

public class LoginEvent extends BaseEvent {
    public LoginEvent(boolean success) {
        super(success);
    }
}

package com.hfut.imlibrary.event;

public class RegisterEvent extends BaseEvent {
    public RegisterEvent(boolean success) {
        super(success);
    }
}

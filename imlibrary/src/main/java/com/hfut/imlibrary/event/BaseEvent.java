package com.hfut.imlibrary.event;

public class BaseEvent {
    private boolean success;

    public BaseEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}

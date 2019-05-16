package com.hfut.imlibrary.event;

public class CreateGroupEvent extends BaseEvent {
    private String id;

    public CreateGroupEvent(String id, boolean success) {
        super(success);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

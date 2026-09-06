package com.beeclient.event.events;

import com.beeclient.event.ClientEvent;

public class ChatEvent extends ClientEvent {
    private String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

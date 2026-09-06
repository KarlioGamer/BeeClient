package com.beeclient.event.events;

import com.beeclient.event.ClientEvent;

public class RenderEvent extends ClientEvent {
    private final float partialTicks;

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

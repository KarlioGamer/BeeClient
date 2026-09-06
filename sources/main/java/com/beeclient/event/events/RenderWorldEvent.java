package com.beeclient.event.events;

import com.beeclient.event.ClientEvent;

public class RenderWorldEvent extends ClientEvent {
    private final float partialTicks;

    public RenderWorldEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

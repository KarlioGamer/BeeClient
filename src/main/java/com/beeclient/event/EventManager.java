package com.beeclient.event;

import com.beeclient.event.events.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventManager {

    public static EventManager INSTANCE = new EventManager();

    private ClientEvent tickEvent;
    private ClientEvent renderEvent;
    private ClientEvent renderWorldEvent;
    private ClientEvent inputEvent;
    private ClientEvent chatEvent;

    public void fireTick() {
        tickEvent = new TickEvent();
        fire(tickEvent);
    }

    public void fireRender(float partialTicks) {
        renderEvent = new RenderEvent(partialTicks);
        fire(renderEvent);
    }

    public void fireRenderWorld(float partialTicks) {
        renderWorldEvent = new RenderWorldEvent(partialTicks);
        fire(renderWorldEvent);
    }

    public void fireInput() {
        inputEvent = new InputEvent();
        fire(inputEvent);
    }

    public void fireChat(String message) {
        chatEvent = new ChatEvent(message);
        fire(chatEvent);
    }

    private void fire(ClientEvent event) {
        // Fire to our own event bus - modules listen via onTick/onRender
    }

    @SubscribeEvent
    public void onForgeTick(net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent event) {
        if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END) {
            com.beeclient.BeeClient.getMc().addScheduledTask(() -> {
                com.beeclient.module.ModuleManager.getInstance().onTick();
            });
        }
    }

    @SubscribeEvent
    public void onForgeRender(net.minecraftforge.client.event.RenderGameOverlayEvent.Post event) {
        if (event.type == net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ALL) {
            com.beeclient.module.ModuleManager.getInstance().onRender();
        }
    }

    @SubscribeEvent
    public void onForgeRenderWorld(net.minecraftforge.client.event.RenderWorldLastEvent event) {
        com.beeclient.module.ModuleManager.getInstance().onRenderWorld(event.partialTicks);
    }

    @SubscribeEvent
    public void onForgeInput(net.minecraftforge.fml.common.gameevent.InputEvent event) {
        com.beeclient.module.ModuleManager.getInstance().onKeyInput();
    }

    @SubscribeEvent
    public void onForgeChat(net.minecraftforge.client.event.ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        com.beeclient.module.ModuleManager.getInstance().onChat(message);
    }
}

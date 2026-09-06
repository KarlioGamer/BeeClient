package com.beeclient.gui;

import com.beeclient.BeeClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GuiKeyHandler {

    private boolean guiOpen = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (BeeClient.OPEN_GUI_KEY.isPressed() && !guiOpen) {
            mc.displayGuiScreen(new ClickGUIScreen());
            guiOpen = true;
        }
        if (mc.currentScreen == null) {
            guiOpen = false;
        }
    }
}
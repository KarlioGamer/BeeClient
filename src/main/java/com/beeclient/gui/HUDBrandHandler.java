package com.beeclient.gui;

import com.beeclient.BeeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDBrandHandler {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (mc.currentScreen != null) return;
        if (mc.gameSettings.showDebugInfo) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int w = sr.getScaledWidth();

        String brand = BeeClient.NAME + " " + BeeClient.VERSION;
        mc.fontRendererObj.drawStringWithShadow(brand, w - mc.fontRendererObj.getStringWidth(brand) - 3, 3, 0xFFFFC400);
    }
}
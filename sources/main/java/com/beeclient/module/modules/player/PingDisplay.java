package com.beeclient.module.modules.player;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

public class PingDisplay extends Module {

    public PingDisplay() {
        super("PingDisplay", "Displays server ping", Category.PLAYER, 0);
    }

    @Override
    public void onRender() {
        if (mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int ping = 0;
        if (mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null) {
            ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
        }
        String text = "Ping: " + ping + "ms";
        int color = ping < 50 ? 0xFF55FF55 : ping < 100 ? 0xFFFFFF55 : 0xFFFF5555;
        RenderUtils.drawString(text, 4, sr.getScaledHeight() - 42, color);
    }
}

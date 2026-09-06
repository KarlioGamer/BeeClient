package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

public class FPSDisplay extends Module {

    public FPSDisplay() {
        super("FPSDisplay", "Displays current FPS counter", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        ScaledResolution sr = new ScaledResolution(mc);
        int fps = mc.getDebugFPS();
        String text = "FPS: " + fps;
        int color = fps >= 60 ? 0xFF55FF55 : fps >= 30 ? 0xFFFFFF55 : 0xFFFF5555;
        RenderUtils.drawString(text, 4, 30, color);
    }
}

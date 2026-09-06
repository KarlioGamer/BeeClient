package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

public class ChromaHUD extends Module {

    private long animOffset = 0;

    public ChromaHUD() {
        super("ChromaHUD", "RGB color effects for HUD elements", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        ScaledResolution sr = new ScaledResolution(mc);
        String text = "Chroma Active";
        int color = ColorUtils.getChromaColor(animOffset);
        RenderUtils.drawString(text, 4, 80, color);
        animOffset++;
    }
}

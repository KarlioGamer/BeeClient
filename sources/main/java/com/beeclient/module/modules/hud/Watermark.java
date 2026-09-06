package com.beeclient.module.modules.hud;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Watermark extends Module {

    public Watermark() {
        super("Watermark", "Shows Bee Client watermark on screen", Category.HUD, 0);
    }

    @Override
    public void onRender() {
        ScaledResolution sr = new ScaledResolution(mc);
        String text = "Bee Client v1.0";
        int x = 4;
        int y = 4;
        int w = RenderUtils.getStringWidth(text) + 6;

        GlStateManager.enableBlend();
        RenderUtils.drawRect(x, y, x + w, y + 13, 0xB00A0D19);
        RenderUtils.drawRect(x, y, x + 2, y + 13, 0xFFFFB703);
        GlStateManager.disableBlend();
        mc.fontRendererObj.drawStringWithShadow(text, x + 4, y + 2, 0xFFFFC400);
    }
}
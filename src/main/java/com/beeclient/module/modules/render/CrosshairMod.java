package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class CrosshairMod extends Module {

    private int crosshairSize = 5;
    private int gap = 2;
    private float thickness = 1.5f;

    public CrosshairMod() {
        super("Crosshair", "Custom crosshair display", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        if (mc.gameSettings.thirdPersonView != 0) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int centerX = sr.getScaledWidth() / 2;
        int centerY = sr.getScaledHeight() / 2;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        int color = ColorUtils.getChromaColor();

        // Top
        RenderUtils.drawRect(centerX - thickness / 2, centerY - gap - crosshairSize, centerX + thickness / 2, centerY - gap, color);
        // Bottom
        RenderUtils.drawRect(centerX - thickness / 2, centerY + gap, centerX + thickness / 2, centerY + gap + crosshairSize, color);
        // Left
        RenderUtils.drawRect(centerX - gap - crosshairSize, centerY - thickness / 2, centerX - gap, centerY + thickness / 2, color);
        // Right
        RenderUtils.drawRect(centerX + gap, centerY - thickness / 2, centerX + gap + crosshairSize, centerY + thickness / 2, color);

        // Center dot
        RenderUtils.drawRect(centerX - 0.5f, centerY - 0.5f, centerX + 0.5f, centerY + 0.5f, 0xFFFFFFFF);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}

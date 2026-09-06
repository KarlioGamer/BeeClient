package com.beeclient.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils {

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        if (left >= right || top >= bottom) return;
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(left, bottom, 0.0).endVertex();
        wr.pos(right, bottom, 0.0).endVertex();
        wr.pos(right, top, 0.0).endVertex();
        wr.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        drawRect((int) left, (int) top, (int) right, (int) bottom, color);
    }

    public static void drawRoundedRect(float x, float y, float w, float h, float radius, int color) {
        if (w <= 0 || h <= 0) return;
        float r = Math.min(radius, Math.min(w, h) / 2.0f);
        int a = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(red / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        // Center cross
        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(x + r, y, 0).endVertex();
        wr.pos(x + w - r, y, 0).endVertex();
        wr.pos(x + w - r, y + h, 0).endVertex();
        wr.pos(x + r, y + h, 0).endVertex();
        tess.draw();
        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(x, y + r, 0).endVertex();
        wr.pos(x + w, y + r, 0).endVertex();
        wr.pos(x + w, y + h - r, 0).endVertex();
        wr.pos(x, y + h - r, 0).endVertex();
        tess.draw();
        // Four corner fans
        drawCornerFan(x + r, y + r, r, 180, tess, wr);
        drawCornerFan(x + w - r, y + r, r, 270, tess, wr);
        drawCornerFan(x + w - r, y + h - r, r, 0, tess, wr);
        drawCornerFan(x + r, y + h - r, r, 90, tess, wr);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void drawCornerFan(float cx, float cy, float r, float startDeg, Tessellator tess, WorldRenderer wr) {
        wr.begin(6, DefaultVertexFormats.POSITION); // TRIANGLE_FAN
        wr.pos(cx, cy, 0).endVertex();
        float endDeg = startDeg + 90;
        for (float deg = startDeg; deg <= endDeg; deg += 4) {
            float rad = (float) Math.toRadians(deg);
            wr.pos(cx + Math.cos(rad) * r, cy + Math.sin(rad) * r, 0).endVertex();
        }
        float endRad = (float) Math.toRadians(endDeg);
        wr.pos(cx + Math.cos(endRad) * r, cy + Math.sin(endRad) * r, 0).endVertex();
        tess.draw();
    }

    public static void drawRoundedRect(int left, int top, int right, int bottom, int radius, int color) {
        drawRoundedRect((float) left, (float) top, (float) (right - left), (float) (bottom - top), radius, color);
    }

    public static void drawHLine(int x1, int x2, int y, int width, int color) {
        drawRect(x1, y, x2, y + width, color);
    }

    public static void drawVLine(int x, int y1, int y2, int width, int color) {
        drawRect(x, y1, x + width, y2, color);
    }

    public static void drawOutlineRect(int x, int y, int w, int h, int thickness, int color) {
        drawRect(x, y, x + w, y + thickness, color);
        drawRect(x, y + h - thickness, x + w, y + h, color);
        drawRect(x, y, x + thickness, y + h, color);
        drawRect(x + w - thickness, y, x + w, y + h, color);
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        int a1 = (color1 >> 24) & 0xFF, r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF, r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(left, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        wr.pos(right, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        wr.pos(right, top, 0.0).color(r1, g1, b1, a1).endVertex();
        wr.pos(left, top, 0.0).color(r1, g1, b1, a1).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientH(int left, int right, int top, int bottom, int color1, int color2) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        int a1 = (color1 >> 24) & 0xFF, r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF, r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(left, bottom, 0.0).color(r1, g1, b1, a1).endVertex();
        wr.pos(left, top, 0.0).color(r1, g1, b1, a1).endVertex();
        wr.pos(right, top, 0.0).color(r2, g2, b2, a2).endVertex();
        wr.pos(right, bottom, 0.0).color(r2, g2, b2, a2).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCenteredString(String text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2, y, color);
    }

    public static void drawString(String text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
    }

    public static int getStringWidth(String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    public static int getScreenCenterX() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2;
    }

    public static int getScreenCenterY() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2;
    }

    public static int getScreenWidth() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static int getScreenHeight() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }
}
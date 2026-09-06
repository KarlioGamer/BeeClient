package com.beeclient.render;

import java.awt.Color;

public class ColorUtils {

    public static int getChromaColor(long offset) {
        float hue = (System.currentTimeMillis() + offset * 10) % 3000L / 3000.0f;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }

    public static int getChromaColor() {
        return getChromaColor(0);
    }

    public static int getRainbowColor(int index, int speed, int saturation) {
        float hue = (System.currentTimeMillis() + index * speed) % (360 * speed) / (360.0f * speed);
        return Color.HSBtoRGB(hue, saturation / 100.0f, 1.0f);
    }

    public static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    public static int fade(int color, int alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return (alpha << 24) | (r << 16) | (g << 8) | b;
    }

    public static Color getHealthColor(float health, float maxHealth) {
        float ratio = health / maxHealth;
        int r = (int) (255 * (1.0f - ratio));
        int g = (int) (255 * ratio);
        return new Color(r, g, 0);
    }
}

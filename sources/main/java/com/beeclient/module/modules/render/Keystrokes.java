package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import com.beeclient.util.ClickCounter;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Keystrokes extends Module {

    private int x = 10;
    private int y = 100;
    private int keySize = 24;
    private int spacing = 2;
    private int bgColor = 0xAA0A0D19;
    private int activeColor = 0xFFFFB703;
    private int textColor = 0xFFEAEAEA;

    public Keystrokes() {
        super("Keystrokes", "Displays WASD keys and CPS on screen", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        ScaledResolution sr = new ScaledResolution(mc);

        int cx = x;
        int cy = y;

        // W
        drawKey(cx + keySize + spacing, cy, keySize, "W", Keyboard.isKeyDown(Keyboard.KEY_W));
        // A
        drawKey(cx, cy + keySize + spacing, keySize, "A", Keyboard.isKeyDown(Keyboard.KEY_A));
        // S
        drawKey(cx + keySize + spacing, cy + keySize + spacing, keySize, "S", Keyboard.isKeyDown(Keyboard.KEY_S));
        // D
        drawKey(cx + (keySize + spacing) * 2, cy + keySize + spacing, keySize, "D", Keyboard.isKeyDown(Keyboard.KEY_D));

        // LMB
        int lmbY = cy + (keySize + spacing) * 2 + spacing;
        drawKey(cx, lmbY, keySize * 2 + spacing, "LMB", Mouse.isButtonDown(0));
        RenderUtils.drawString("\u00a77" + ClickCounter.INSTANCE.getLmbCps() + " \u00a7ecps", cx + keySize * 2 + spacing + 5, lmbY + 7, textColor);

        // RMB
        int rmbY = lmbY;
        drawKey(cx + (keySize * 2 + spacing) + keySize * 2 + spacing + 40, rmbY, keySize * 2 + spacing, "RMB", Mouse.isButtonDown(1));
    }

    private void drawKey(int x, int y, int size, String text, boolean active) {
        int color = active ? activeColor : bgColor;
        GlStateManager.enableBlend();
        RenderUtils.drawRect(x, y, x + size, y + size, color);
        if (active) {
            com.beeclient.render.RenderUtils.drawRect(x, y + size - 2, x + size, y + size, 0xFF8A5A00);
        }
        GlStateManager.disableBlend();

        int textColorFinal = active ? 0xFF0A0D19 : 0xFFAAAAAA;
        int textWidth = mc.fontRendererObj.getStringWidth(text);
        mc.fontRendererObj.drawString(text, x + size / 2 - textWidth / 2, y + size / 2 - 3, textColorFinal);
    }
}
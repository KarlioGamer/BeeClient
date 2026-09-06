package com.beeclient.module.modules.movement;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ToggleSprint extends Module {

    public ToggleSprint() {
        super("ToggleSprint", "Sprint toggle with a key press", Category.MOVEMENT, Keyboard.KEY_R);
        addSetting(new Setting<>("Only Forward", true));
        addSetting(new Setting<>("Show HUD", true));
    }

    @Override
    protected void onDisable() {
        if (mc.thePlayer != null) {
            int key = mc.gameSettings.keyBindSprint.getKeyCode();
            boolean physical = Keyboard.isKeyDown(key);
            KeyBinding.setKeyBindState(key, physical);
            if (!physical && mc.thePlayer.isSprinting()) {
                mc.thePlayer.setSprinting(false);
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.thePlayer == null) return;

        boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
        boolean onlyForward = getSetting("Only Forward").isBoolean() && getSetting("Only Forward").getBoolean();
        boolean wantSprint;

        if (forward) {
            wantSprint = true;
        } else if (onlyForward) {
            wantSprint = false;
        } else {
            wantSprint = mc.thePlayer.movementInput.moveForward > 0.01f || mc.thePlayer.isSprinting();
        }

        if (wantSprint) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()));
        }
    }

    @Override
    public void onRender() {
        if (getSetting("Show HUD").isBoolean() && getSetting("Show HUD").getBoolean()) {
            ScaledResolution sr = new ScaledResolution(mc);
            RenderUtils.drawString("\u00a7eSprinting", 4, 90, 0xFFFFFFAA);
        }
    }
}
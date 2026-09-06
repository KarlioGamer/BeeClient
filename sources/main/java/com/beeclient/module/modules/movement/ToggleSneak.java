package com.beeclient.module.modules.movement;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ToggleSneak extends Module {

    private boolean sneaking = false;

    public ToggleSneak() {
        super("ToggleSneak", "Toggle sneak with a key press", Category.MOVEMENT, Keyboard.KEY_C);
    }

    @Override
    protected void onEnable() {
        sneaking = true;
    }

    @Override
    protected void onDisable() {
        sneaking = false;
        if (mc.thePlayer != null) {
            int key = mc.gameSettings.keyBindSneak.getKeyCode();
            KeyBinding.setKeyBindState(key, Keyboard.isKeyDown(key));
        }
    }

    @Override
    public void onTick() {
        if (mc.thePlayer != null && sneaking) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }

    @Override
    public void onRender() {
        if (mc.thePlayer != null && sneaking && mc.thePlayer.isSneaking()) {
            ScaledResolution sr = new ScaledResolution(mc);
            RenderUtils.drawString("Sneaking", 4, 100, 0xFFAAAAAA);
        }
    }
}

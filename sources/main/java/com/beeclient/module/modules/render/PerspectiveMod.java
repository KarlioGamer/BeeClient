package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import org.lwjgl.input.Keyboard;

public class PerspectiveMod extends Module {

    private float yaw;
    private float pitch;
    private boolean perspectiveActive = false;

    public PerspectiveMod() {
        super("Perspective", "360 degree camera view", Category.RENDER, Keyboard.KEY_L);
    }

    @Override
    protected void onEnable() {
        if (mc.thePlayer != null) {
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.rotationPitch;
            perspectiveActive = true;
        }
    }

    @Override
    protected void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
            perspectiveActive = false;
        }
    }

    @Override
    public void onTick() {
        if (perspectiveActive && mc.thePlayer != null) {
            mc.gameSettings.thirdPersonView = 1;
        }
    }

    public void setRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}

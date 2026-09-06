package com.beeclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class MinecraftUtils {

    public static boolean isMoving() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.gameSettings.keyBindForward.isKeyDown() ||
               mc.gameSettings.keyBindBack.isKeyDown() ||
               mc.gameSettings.keyBindLeft.isKeyDown() ||
               mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static boolean isOnGround() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.thePlayer != null && mc.thePlayer.onGround;
    }

    public static double getDirection() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return 0;
        float yaw = mc.thePlayer.rotationYaw;
        return Math.toRadians(yaw);
    }
}

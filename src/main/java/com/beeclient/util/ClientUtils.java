package com.beeclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientUtils {

    public static boolean isPlayer() {
        return Minecraft.getMinecraft().thePlayer != null;
    }

    public static boolean isWorld() {
        return Minecraft.getMinecraft().theWorld != null;
    }

    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public static int getFPS() {
        return Minecraft.getMinecraft().getDebugFPS();
    }

    public static String stripColor(String text) {
        return text.replaceAll("\u00a7[0-9a-fk-orA-FK-OR]", "");
    }
}

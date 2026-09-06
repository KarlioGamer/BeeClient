package com.beeclient.module.modules.misc;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.util.DiscordRPCHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;

public class DiscordRP extends Module {

    private boolean wasInWorld = false;
    private String lastServerIP = null;

    public DiscordRP() {
        super("DiscordRP", "Discord Rich Presence", Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        DiscordRPCHandler.init();
        wasInWorld = false;
        lastServerIP = null;
    }

    @Override
    public void onDisable() {
        DiscordRPCHandler.shutdown();
    }

    @Override
    public void onTick() {
        if (!DiscordRPCHandler.isInitialized()) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        WorldClient world = mc.theWorld;

        boolean inWorld = (world != null && player != null);

        if (inWorld && !wasInWorld) {
            ServerData server = mc.getCurrentServerData();
            if (server != null) {
                lastServerIP = server.serverIP;
                DiscordRPCHandler.updateMultiplayer(server.serverName, server.serverIP);
            } else {
                lastServerIP = null;
                DiscordRPCHandler.updateSingleplayer(
                    mc.theWorld.getWorldInfo().getWorldName()
                );
            }
        } else if (!inWorld && wasInWorld) {
            lastServerIP = null;
            DiscordRPCHandler.updateMainMenu();
        }

        wasInWorld = inWorld;
    }
}

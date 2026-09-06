package com.beeclient.event;

import com.beeclient.BeeClient;
import com.beeclient.util.DiscordRPCHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class DiscordRPCEvents {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        ServerData server = mc.getCurrentServerData();
        if (server != null) {
            DiscordRPCHandler.updateMultiplayer(server.serverName, server.serverIP);
        } else {
            DiscordRPCHandler.updateSingleplayer(
                mc.theWorld.getWorldInfo().getWorldName()
            );
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        DiscordRPCHandler.updateMainMenu();
    }
}

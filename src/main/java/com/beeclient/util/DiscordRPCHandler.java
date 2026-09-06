package com.beeclient.util;

import dev.firstdark.rpc.DiscordRpc;
import dev.firstdark.rpc.enums.ActivityType;
import dev.firstdark.rpc.enums.ErrorCode;
import dev.firstdark.rpc.handlers.RPCEventHandler;
import dev.firstdark.rpc.models.DiscordRichPresence;
import dev.firstdark.rpc.models.User;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.World;

public class DiscordRPCHandler {

    private static final String APP_ID = "1275410027335733308";
    private static DiscordRpc rpc;
    private static boolean initialized = false;
    private static long startTime;

    public static void init() {
        if (initialized) return;
        try {
            rpc = new DiscordRpc();
            rpc.setDebugMode(true);
            startTime = System.currentTimeMillis() / 1000;

            RPCEventHandler handler = new RPCEventHandler() {
                @Override
                public void ready(User user) {
                    System.out.println("[Bee Client] Discord RPC connected as " + user.getUsername());
                    updateMainMenu();
                }

                @Override
                public void disconnected(ErrorCode errorCode, String message) {
                    System.out.println("[Bee Client] Discord RPC disconnected: " + message);
                }

                @Override
                public void errored(ErrorCode errorCode, String message) {
                    System.err.println("[Bee Client] Discord RPC error: " + errorCode + " - " + message);
                }
            };

            rpc.init(APP_ID, handler, false);
            initialized = true;
            System.out.println("[Bee Client] Discord RPC initialized");
        } catch (Exception e) {
            System.err.println("[Bee Client] Failed to initialize Discord RPC: " + e.getMessage());
        }
    }

    public static void shutdown() {
        if (rpc != null) {
            try {
                rpc.shutdown();
            } catch (Exception e) {
                System.err.println("[Bee Client] Error shutting down Discord RPC: " + e.getMessage());
            }
            rpc = null;
            initialized = false;
        }
    }

    public static void updateMainMenu() {
        if (!initialized || rpc == null) return;
        DiscordRichPresence presence = DiscordRichPresence.builder()
                .details("In Main Menu")
                .state("Idle")
                .largeImageKey("minecraft")
                .largeImageText("Bee Client v1.0 - Minecraft 1.8.9")
                .startTimestamp(startTime)
                .activityType(ActivityType.PLAYING)
                .build();
        rpc.updatePresence(presence);
    }

    public static void updateSingleplayer(String worldName) {
        if (!initialized || rpc == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        String gameType = "Survival";
        if (mc.thePlayer != null) {
            gameType = mc.thePlayer.capabilities.isCreativeMode ? "Creative" :
                       mc.thePlayer.capabilities.allowFlying ? "Spectator" : "Survival";
        }
        DiscordRichPresence presence = DiscordRichPresence.builder()
                .details("Playing Singleplayer")
                .state(worldName + " - " + gameType)
                .largeImageKey("minecraft")
                .largeImageText("Bee Client v1.0")
                .smallImageKey("singleplayer")
                .smallImageText(gameType)
                .startTimestamp(startTime)
                .activityType(ActivityType.PLAYING)
                .build();
        rpc.updatePresence(presence);
    }

    public static void updateMultiplayer(String serverName, String serverIP) {
        if (!initialized || rpc == null) return;
        DiscordRichPresence presence = DiscordRichPresence.builder()
                .details("Playing Multiplayer")
                .state(serverName)
                .largeImageKey("minecraft")
                .largeImageText(serverIP != null ? serverIP : "Minecraft 1.8.9")
                .smallImageKey("multiplayer")
                .smallImageText("Multiplayer")
                .startTimestamp(startTime)
                .activityType(ActivityType.PLAYING)
                .build();
        rpc.updatePresence(presence);
    }

    public static void updateServer(String serverIP) {
        if (!initialized || rpc == null) return;
        DiscordRichPresence presence = DiscordRichPresence.builder()
                .details("Playing on Server")
                .state(serverIP)
                .largeImageKey("minecraft")
                .largeImageText("Bee Client v1.0")
                .smallImageKey("multiplayer")
                .smallImageText("Multiplayer")
                .startTimestamp(startTime)
                .activityType(ActivityType.PLAYING)
                .build();
        rpc.updatePresence(presence);
    }

    public static void updateWorld() {
        if (!initialized || rpc == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        World world = mc.theWorld;
        String worldName = "Unknown";
        if (world != null) {
            worldName = world.getWorldInfo().getWorldName();
        }

        String gameType = mc.thePlayer.capabilities.isCreativeMode ? "Creative" : "Survival";
        int players = world != null ? world.playerEntities.size() : 1;

        String details = mc.isIntegratedServerRunning() ? "Playing Singleplayer" : "Playing Multiplayer";
        String state = worldName + " | " + gameType + " | " + players + " players";

        DiscordRichPresence presence = DiscordRichPresence.builder()
                .details(details)
                .state(state)
                .largeImageKey("minecraft")
                .largeImageText("Bee Client v1.0")
                .startTimestamp(startTime)
                .activityType(ActivityType.PLAYING)
                .build();
        rpc.updatePresence(presence);
    }

    public static boolean isInitialized() {
        return initialized;
    }
}

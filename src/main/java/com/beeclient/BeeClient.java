package com.beeclient;

import com.beeclient.module.ModuleManager;
import com.beeclient.gui.GuiKeyHandler;
import com.beeclient.event.EventManager;
import com.beeclient.util.DiscordRPCHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

public class BeeClient {

    public static final String MODID = "beeclient";
    public static final String NAME = "Bee Client";
    public static final String VERSION = "1.0";

    public static final KeyBinding OPEN_GUI_KEY = new KeyBinding("Open Bee Client GUI", Keyboard.KEY_RSHIFT, "Bee Client");

    public static BeeClient instance;

    private ModuleManager moduleManager;
    private EventManager eventManager;

    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
    }

    public void init(FMLInitializationEvent event) {
        if (instance == null) instance = this;

        eventManager = new EventManager();
        MinecraftForge.EVENT_BUS.register(eventManager);

        ClientRegistry.registerKeyBinding(OPEN_GUI_KEY);

        moduleManager = new ModuleManager();
        moduleManager.init();
        com.beeclient.config.ConfigManager.INSTANCE.load();

        MinecraftForge.EVENT_BUS.register(new com.beeclient.gui.MainMenuHandler());
        MinecraftForge.EVENT_BUS.register(new GuiKeyHandler());
        MinecraftForge.EVENT_BUS.register(new com.beeclient.event.DiscordRPCEvents());
        MinecraftForge.EVENT_BUS.register(new com.beeclient.gui.HUDBrandHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DiscordRPCHandler.shutdown();
            com.beeclient.config.ConfigManager.INSTANCE.save();
        }));
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public static Minecraft getMc() {
        return Minecraft.getMinecraft();
    }
}

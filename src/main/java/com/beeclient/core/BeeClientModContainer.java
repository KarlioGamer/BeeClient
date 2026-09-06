package com.beeclient.core;

import com.beeclient.BeeClient;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class BeeClientModContainer extends DummyModContainer {

    public BeeClientModContainer() {
        super(createModInfo());
    }

    private static ModMetadata createModInfo() {
        ModMetadata meta = new ModMetadata();
        meta.modId = "beeclient";
        meta.name = "Bee Client";
        meta.description = "Bee Client - Minecraft 1.8.9 Client";
        meta.version = "1.0";
        meta.authorList = Arrays.asList("Bee Client Team");
        return meta;
    }

    @Override
    public File getSource() {
        try {
            URL url = BeeClientModContainer.class.getProtectionDomain().getCodeSource().getLocation();
            return new File(url.toURI());
        } catch (Exception e) {
            try {
                URL url = BeeClientModContainer.class.getProtectionDomain().getCodeSource().getLocation();
                return new File(url.getPath());
            } catch (Exception e2) {
                System.err.println("[Bee Client] Failed to get source file: " + e2.getMessage());
                return null;
            }
        }
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void onPreInit(FMLPreInitializationEvent event) {
        BeeClient.instance = new BeeClient();
        BeeClient.instance.preInit(event);
    }

    @Subscribe
    public void onInit(FMLInitializationEvent event) {
        BeeClient.instance.init(event);
    }
}

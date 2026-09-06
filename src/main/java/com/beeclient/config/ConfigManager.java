package com.beeclient.config;

import com.beeclient.module.Module;
import com.beeclient.module.ModuleManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ConfigManager {

    public static final ConfigManager INSTANCE = new ConfigManager();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private File file;

    private ConfigManager() {}

    private File getFile() {
        if (file == null) {
            file = new File(Minecraft.getMinecraft().mcDataDir, "beeclient/config.json");
        }
        return file;
    }

    public void load() {
        File f = getFile();
        if (!f.exists()) return;

        try (Reader reader = new FileReader(f)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            JsonObject modules = root.getAsJsonObject("modules");
            if (modules == null) return;

            for (Module m : ModuleManager.getInstance().getModules()) {
                JsonObject data = modules.getAsJsonObject(m.getName());
                if (data == null) continue;

                if (data.has("key") && !data.get("key").isJsonNull()) {
                    m.setKeyBind(data.get("key").getAsInt());
                }

                JsonObject settings = data.getAsJsonObject("settings");
                if (settings != null) {
                    for (Module.Setting<?> s : m.getSettings()) {
                        JsonElement v = settings.get(s.getName());
                        if (v == null || v.isJsonNull()) continue;
                        if (s.isNumber()) {
                            s.setNumber(v.getAsNumber());
                        } else if (s.isBoolean()) {
                            s.setBoolean(v.getAsBoolean());
                        }
                    }
                }

                if (data.has("enabled") && data.get("enabled").getAsBoolean()) {
                    m.enable();
                }
            }
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JsonObject root = new JsonObject();
        JsonObject modules = new JsonObject();

        for (Module m : ModuleManager.getInstance().getModules()) {
            JsonObject data = new JsonObject();
            data.addProperty("enabled", m.isEnabled());
            data.addProperty("key", m.getKeyBind());

            JsonObject settings = new JsonObject();
            for (Module.Setting<?> s : m.getSettings()) {
                if (s.isBoolean()) {
                    settings.addProperty(s.getName(), s.getBoolean());
                } else if (s.isNumber()) {
                    settings.addProperty(s.getName(), s.getNumber());
                }
            }
            data.add("settings", settings);

            modules.add(m.getName(), data);
        }
        root.add("modules", modules);

        File f = getFile();
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (Writer writer = new FileWriter(f)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
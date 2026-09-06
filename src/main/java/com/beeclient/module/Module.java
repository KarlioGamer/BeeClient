package com.beeclient.module;

import com.beeclient.BeeClient;
import com.beeclient.event.Event;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    protected Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private String description;
    private Category category;
    private int keyBind;
    private boolean enabled;
    private boolean visible = true;
    private List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, String description, Category category, int keyBind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBind = keyBind;
        this.enabled = false;
    }

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
        com.beeclient.config.ConfigManager.INSTANCE.save();
    }

    public void enable() {
        enabled = true;
        onEnable();
    }

    public void disable() {
        enabled = false;
        onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}
    public void onTick() {}
    public void onRender() {}
    public void onRenderWorld(float partialTicks) {}
    public void onChat(String message) {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public int getKeyBind() { return keyBind; }
    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
        com.beeclient.config.ConfigManager.INSTANCE.save();
    }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public List<Setting<?>> getSettings() { return settings; }

    protected void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public Setting<?> getSetting(String name) {
        for (Setting<?> s : settings) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public static class Setting<T> {
        private String name;
        private T value;
        private T min;
        private T max;

        public Setting(String name, T value) {
            this.name = name;
            this.value = value;
        }

        public Setting(String name, T value, T min, T max) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public String getName() { return name; }
        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
        public T getMin() { return min; }
        public T getMax() { return max; }
        public boolean isBoolean() { return value instanceof Boolean; }
        public boolean isNumber() { return value instanceof Number; }
        public boolean getBoolean() { return (Boolean) value; }
        public Number getNumber() { return (Number) value; }
        public void setNumber(Number number) {
            if (value instanceof Double) {
                value = (T) Double.valueOf(number.doubleValue());
            } else if (value instanceof Integer) {
                value = (T) Integer.valueOf(number.intValue());
            } else if (value instanceof Float) {
                value = (T) Float.valueOf(number.floatValue());
            } else if (value instanceof Long) {
                value = (T) Long.valueOf(number.longValue());
            }
        }
        public void setBoolean(boolean b) {
            if (value instanceof Boolean) {
                value = (T) Boolean.valueOf(b);
            }
        }
        public double normalized(Number n) {
            if (min == null || max == null || min instanceof Number == false) return 0.0;
            double mn = ((Number) min).doubleValue();
            double mx = ((Number) max).doubleValue();
            if (mx <= mn) return 0.0;
            return (n.doubleValue() - mn) / (mx - mn);
        }
        public Number denormalized(double t) {
            double mn = ((Number) min).doubleValue();
            double mx = ((Number) max).doubleValue();
            double v = mn + (mx - mn) * t;
            if (value instanceof Integer || value instanceof Long) {
                return Long.valueOf(Math.round(v));
            }
            return Double.valueOf(v);
        }
    }
}

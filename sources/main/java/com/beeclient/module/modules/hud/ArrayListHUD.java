package com.beeclient.module.modules.hud;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.module.ModuleManager;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArrayListHUD extends Module {

    public ArrayListHUD() {
        super("ArrayList", "Shows enabled modules as a list", Category.HUD, 0);
        addSetting(new Setting<>("Sort Alphabetical", false));
    }

    @Override
    public void onRender() {
        ScaledResolution sr = new ScaledResolution(mc);
        List<Module> enabled = new ArrayList<>();
        for (Module m : ModuleManager.getInstance().getModules()) {
            if (m.isEnabled() && m.isVisible() && !"Watermark".equals(m.getName()) && !"ArrayList".equals(m.getName())) {
                enabled.add(m);
            }
        }
        if (enabled.isEmpty()) return;

        if (getSetting("Sort Alphabetical").isBoolean() && getSetting("Sort Alphabetical").getBoolean()) {
            enabled.sort(new Comparator<Module>() {
                @Override
                public int compare(Module a, Module b) {
                    return a.getName().compareToIgnoreCase(b.getName());
                }
            });
        }

        int y = 2;
        int screenWidth = sr.getScaledWidth();
        int maxW = 0;
        for (Module m : enabled) {
            maxW = Math.max(maxW, mc.fontRendererObj.getStringWidth(m.getName()) + 14);
        }

        GlStateManager.enableBlend();
        for (int i = 0; i < enabled.size(); i++) {
            Module m = enabled.get(i);
            String name = m.getName();
            int w = mc.fontRendererObj.getStringWidth(name) + 12;

            int leftBarColor = categoryColor(m.getCategory());
            int textColor = 0xFFFFC400;
            if (m.getCategory() == Category.COMBAT) textColor = 0xFFFF5D6C;
            else if (m.getCategory() == Category.RENDER) textColor = 0xFFA78BFA;
            else if (m.getCategory() == Category.MOVEMENT) textColor = 0xFF38BDF8;
            else if (m.getCategory() == Category.PLAYER) textColor = 0xFF34D399;
            else if (m.getCategory() == Category.HUD) textColor = 0xFFFBBF24;
            else textColor = 0xFFF472B6;

            RenderUtils.drawRect(screenWidth - w - 4, y, screenWidth, y + 12, 0xCC0A0D19);
            RenderUtils.drawRect(screenWidth - w - 4, y, screenWidth - w - 2, y + 12, leftBarColor);
            mc.fontRendererObj.drawStringWithShadow(name, screenWidth - w + 1, y + 1, textColor);
            y += 12;
        }
        GlStateManager.disableBlend();
    }

    private int categoryColor(Category category) {
        switch (category) {
            case COMBAT: return 0xFFFF5D6C;
            case RENDER: return 0xFFA78BFA;
            case MOVEMENT: return 0xFF38BDF8;
            case PLAYER: return 0xFF34D399;
            case HUD: return 0xFFFBBF24;
            default: return 0xFFF472B6;
        }
    }
}
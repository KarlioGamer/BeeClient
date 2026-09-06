package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class PotionHUD extends Module {

    public PotionHUD() {
        super("PotionHUD", "Displays active potion effects with timers", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        if (mc.thePlayer == null) return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int x = 4;
        int y = 40;

        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName());
            int amplifier = effect.getAmplifier();
            int duration = effect.getDuration();

            String timeStr = formatDuration(duration);
            String text = name;
            if (amplifier > 0) text += " " + (amplifier + 1);
            text += " " + timeStr;

            int color = potion.getLiquidColor();
            RenderUtils.drawString(text, x, y, color);
            y += 12;
        }
    }

    private String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        if (minutes > 0) {
            return minutes + ":" + String.format("%02d", seconds);
        }
        return seconds + "s";
    }
}

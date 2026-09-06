package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.ScaledResolution;

import java.util.HashMap;
import java.util.Map;

public class DamageIndicators extends Module {

    private Map<Entity, float[]> prevHealth = new HashMap<>();

    public DamageIndicators() {
        super("DamageIndicators", "Shows damage dealt to entities", Category.RENDER, 0);
    }

    @Override
    public void onTick() {
        if (mc.theWorld == null) return;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                float health = player.getHealth();

                if (prevHealth.containsKey(entity)) {
                    float prev = prevHealth.get(entity)[0];
                    if (prev > health) {
                        float damage = prev - health;
                    }
                }
                prevHealth.put(entity, new float[]{health});
            }
        }
    }
}

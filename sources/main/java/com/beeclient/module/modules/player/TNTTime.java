package com.beeclient.module.modules.player;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.ScaledResolution;

public class TNTTime extends Module {

    public TNTTime() {
        super("TNTTime", "Shows TNT countdown timer", Category.PLAYER, 0);
    }

    @Override
    public void onRender() {
        if (mc.theWorld == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int y = 120;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed) {
                EntityTNTPrimed tnt = (EntityTNTPrimed) entity;
                int fuse = tnt.fuse;
                float seconds = fuse / 20.0f;
                String text = String.format("TNT: %.1fs", seconds);
                int color = seconds < 1.0 ? 0xFFFF5555 : 0xFFFFFF55;
                RenderUtils.drawString(text, 4, y, color);
                y += 12;
            }
        }
    }
}

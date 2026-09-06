package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

public class DirectionHUD extends Module {

    public DirectionHUD() {
        super("DirectionHUD", "Displays compass with cardinal directions", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        if (mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        float yaw = mc.thePlayer.rotationYaw;
        String direction = getDirection(yaw);

        int centerX = sr.getScaledWidth() / 2;
        int y = 2;

        int color = ColorUtils.getChromaColor();
        mc.fontRendererObj.drawStringWithShadow(direction, centerX - mc.fontRendererObj.getStringWidth(direction) / 2, y, color);
    }

    private String getDirection(float yaw) {
        yaw = yaw % 360;
        if (yaw < 0) yaw += 360;

        if (yaw >= 315 || yaw < 45) return "S";
        if (yaw >= 45 && yaw < 135) return "W";
        if (yaw >= 135 && yaw < 225) return "N";
        if (yaw >= 225 && yaw < 315) return "E";
        return "S";
    }
}

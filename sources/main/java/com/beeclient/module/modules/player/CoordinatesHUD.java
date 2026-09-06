package com.beeclient.module.modules.player;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;

public class CoordinatesHUD extends Module {

    public CoordinatesHUD() {
        super("Coordinates", "Displays X, Y, Z coordinates", Category.PLAYER, 0);
    }

    @Override
    public void onRender() {
        if (mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        BlockPos pos = mc.thePlayer.getPosition();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        String text = String.format("XYZ: %d / %d / %d  C: %d, %d", x, y, z, chunkX, chunkZ);
        RenderUtils.drawString(text, 4, sr.getScaledHeight() - 30, 0xFFEAEAEA);
    }
}

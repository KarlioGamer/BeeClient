package com.beeclient.module.modules.combat;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReachDisplay extends Module {

    private double lastReach = -1;

    public ReachDisplay() {
        super("ReachDisplay", "Shows the distance of your last hit (display only)", Category.COMBAT, 0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!isEnabled()) return;
        if (mc.thePlayer == null || event.entityPlayer != mc.thePlayer) return;

        lastReach = Math.round(edgeDistance(event.target) * 100.0) / 100.0;
    }

    private double edgeDistance(Entity entity) {
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        double px = mc.thePlayer.posX;
        double py = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double pz = mc.thePlayer.posZ;

        double dx = Math.max(bb.minX - px, 0.0D) - Math.max(px - bb.maxX, 0.0D);
        double dy = Math.max(bb.minY - py, 0.0D) - Math.max(py - bb.maxY, 0.0D);
        double dz = Math.max(bb.minZ - pz, 0.0D) - Math.max(pz - bb.maxZ, 0.0D);
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public void onRender() {
        if (mc.thePlayer == null) return;
        ScaledResolution sr = new ScaledResolution(mc);
        String text = lastReach >= 0
                ? "Hit: " + String.format("%.2f", lastReach) + "m"
                : "Hit: --";
        RenderUtils.drawString(text, 4, 60, ColorUtils.getChromaColor());
    }
}
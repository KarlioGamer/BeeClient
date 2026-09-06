package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.ColorUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class BlockOverlay extends Module {

    private int color = 0xFF533483;
    private float lineWidth = 2.0f;

    public BlockOverlay() {
        super("BlockOverlay", "Custom block selection outline", Category.RENDER, 0);
    }

    @Override
    public void onRenderWorld(float partialTicks) {
        if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null) return;

        BlockPos pos = mc.objectMouseOver.getBlockPos();
        double x = pos.getX() - (mc.getRenderManager().viewerPosX);
        double y = pos.getY() - (mc.getRenderManager().viewerPosY);
        double z = pos.getZ() - (mc.getRenderManager().viewerPosZ);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(lineWidth);

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        GlStateManager.color(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        wr.pos(x, y, z).endVertex();
        wr.pos(x + 1, y, z).endVertex();
        wr.pos(x + 1, y, z + 1).endVertex();
        wr.pos(x, y, z + 1).endVertex();
        wr.pos(x, y, z).endVertex();

        wr.pos(x, y + 1, z).endVertex();
        wr.pos(x + 1, y + 1, z).endVertex();
        wr.pos(x + 1, y + 1, z + 1).endVertex();
        wr.pos(x, y + 1, z + 1).endVertex();
        wr.pos(x, y + 1, z).endVertex();

        wr.pos(x, y, z).endVertex();
        tessellator.draw();

        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        wr.pos(x, y, z).endVertex();
        wr.pos(x, y + 1, z).endVertex();
        wr.pos(x + 1, y, z).endVertex();
        wr.pos(x + 1, y + 1, z).endVertex();
        wr.pos(x + 1, y, z + 1).endVertex();
        wr.pos(x + 1, y + 1, z + 1).endVertex();
        wr.pos(x, y, z + 1).endVertex();
        wr.pos(x, y + 1, z + 1).endVertex();
        tessellator.draw();

        GL11.glLineWidth(1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}

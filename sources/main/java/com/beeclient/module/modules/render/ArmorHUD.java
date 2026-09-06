package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Module {

    public ArmorHUD() {
        super("ArmorHUD", "Displays armor durability on screen", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        if (mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int centerX = sr.getScaledWidth() / 2;

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        RenderHelper.enableGUIStandardItemLighting();

        int y = sr.getScaledHeight() - 20;
        int spacing = 20;

        for (int i = 0; i < 4; i++) {
            ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
            if (stack != null) {
                int x = centerX - 40 + i * spacing;
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);

                int damage = stack.getMaxDamage() - stack.getItemDamage();
                String durability = String.valueOf(damage);
                mc.fontRendererObj.drawStringWithShadow(durability, x + 8 - mc.fontRendererObj.getStringWidth(durability) / 2, y - 10, 0xFFFFFF);
            }
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
    }
}

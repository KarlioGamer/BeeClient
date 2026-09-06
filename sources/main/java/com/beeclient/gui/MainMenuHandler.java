package com.beeclient.gui;

import com.beeclient.render.TextureLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class MainMenuHandler {

    private static int bgTexture = -1;
    private static int logoTexture = -1;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new BeeMainMenu();
        }
    }

    public static class BeeMainMenu extends GuiMainMenu {

        private static final int ACCENT = 0xFFF7C948;
        private static final int WHITE = 0xFFEAEAEA;
        private static final int GRAY = 0xFFAAAAAA;

        @Override
        public void initGui() {
            super.initGui();
            if (bgTexture == -1) {
                bgTexture = TextureLoader.loadTexture("/assets/beeclient/textures/gui/bg.png");
            }
            if (logoTexture == -1) {
                logoTexture = TextureLoader.loadTexture("/assets/beeclient/textures/logo.png");
            }
            this.buttonList.add(0, new GuiButton(9999, this.width / 2 - 100, this.height / 4 + 48, 200, 20, "\u00a7e\u00a7lBee Client \u00a77\u25b8 Mods"));
        }

        @Override
        protected void actionPerformed(GuiButton button) throws java.io.IOException {
            if (button.id == 9999) {
                Minecraft.getMinecraft().displayGuiScreen(new ClickGUIScreen());
            } else {
                super.actionPerformed(button);
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (bgTexture > 0) {
                TextureLoader.bind(bgTexture);
                drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, 1920, 1080);
            }

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            if (logoTexture > 0) {
                TextureLoader.bind(logoTexture);
                drawModalRectWithCustomSizedTexture(this.width / 2 - 28, 14, 0, 0, 56, 56, 512, 512);
            }
            GlStateManager.disableBlend();

            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 1.0F);
            String title = "Bee Client";
            mc.fontRendererObj.drawStringWithShadow(title, this.width / 4.0F - mc.fontRendererObj.getStringWidth(title) / 2.0F, 38.0F, ACCENT);
            GlStateManager.popMatrix();

            String subtitle = "Premium Client v1.0 - Minecraft 1.8.9";
            mc.fontRendererObj.drawStringWithShadow(subtitle, this.width / 2 - mc.fontRendererObj.getStringWidth(subtitle) / 2, 96, WHITE);

            int lineY = 108;
            com.beeclient.render.RenderUtils.drawRect(this.width / 2 - 120, lineY, this.width / 2 + 120, lineY + 1, ACCENT);

            for (GuiButton button : this.buttonList) {
                button.drawButton(mc, mouseX, mouseY);
            }

            String footer = "v1.0 \u00a77\u2013 47 Mods \u2013 \u00a7eForge 1.8.9";
            mc.fontRendererObj.drawStringWithShadow(footer, this.width / 2 - mc.fontRendererObj.getStringWidth(footer) / 2, this.height - 18, GRAY);
        }
    }
}

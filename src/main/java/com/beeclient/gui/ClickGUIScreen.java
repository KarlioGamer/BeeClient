package com.beeclient.gui;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.module.ModuleManager;
import com.beeclient.render.RenderUtils;
import com.beeclient.render.TextureLoader;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class ClickGUIScreen extends GuiScreen {

    // ==================== COLORS (HTML exact) ====================
    private static final int C_TEXT = 0xFFF0F6FC;
    private static final int C_MUTED = 0xFF8B949E;
    private static final int C_INDIGO = 0xFF6366F1;
    private static final int C_INDIGO_DARK = 0xFF4F46E5;
    private static final int C_INDIGO_LIGHT = 0xFF818CF8;
    private static final int C_GREEN = 0xFF22C55E;
    private static final int C_WHITE = 0xFFFFFFFF;

    // Panel
    private static final int PANEL_BG = 0xBF161B22;
    private static final int PANEL_BORDER = 0x1AFFFFFF;
    private static final int PANEL_INNER = 0x1AFFFFFF;

    // Header
    private static final int HEADER_BG = 0x660D1117;
    private static final int HEADER_BORDER = 0x14FFFFFF;

    // Sidebar
    private static final int SIDEBAR_BG = 0x330D1117;
    private static final int SIDEBAR_BORDER = 0x14FFFFFF;
    private static final int NAV_HOVER = 0x0DFFFFFF;
    private static final int NAV_ACTIVE_GRADIENT_L = 0x266366F1;
    private static final int NAV_ACTIVE_GRADIENT_R = 0x00000000;

    // Cards
    private static final int CARD_BG = 0x08FFFFFF;
    private static final int CARD_BORDER = 0x0FFFFFFF;
    private static final int CARD_HOVER_BG = 0x0FFFFFFF;
    private static final int CARD_HOVER_BORDER = 0x26FFFFFF;
    private static final int CARD_ACTIVE_BG = 0x0A6366F1;
    private static final int CARD_ACTIVE_BORDER = 0x666366F1;

    // Switch
    private static final int SW_OFF = 0x1AFFFFFF;
    private static final int SW_ON = 0xFF6366F1;
    private static final int KNOB_OFF = 0xFF8B949E;

    // Search
    private static final int SEARCH_BG = 0x0DFFFFFF;
    private static final int SEARCH_BORDER = 0x1AFFFFFF;
    private static final int SEARCH_FOCUS_BG = 0x14FFFFFF;
    private static final int SEARCH_FOCUS_BORDER = 0xFF6366F1;

    // Footer
    private static final int FOOTER_BG = 0x990D1117;
    private static final int FOOTER_BORDER = 0x14FFFFFF;

    // Scrollbar
    private static final int SCROLL_THUMB = 0x26FFFFFF;
    private static final int SCROLL_THUMB_HOVER = 0x40FFFFFF;

    private static final int LOGO_TEXTURE = TextureLoader.loadTexture("/assets/beeclient/textures/logo.png");

    // ==================== LAYOUT ====================
    private int panelX, panelY, panelW, panelH;
    private int headerY, headerH;
    private int sidebarX, sidebarY, sidebarW, sidebarH;
    private int contentX, contentY, contentW, contentH;
    private int footerY, footerH;
    private int searchX, searchY, searchW, searchH;

    private static final int HEADER_H = 58;
    private static final int SIDEBAR_W = 220;
    private static final int FOOTER_H = 44;
    private static final int CARD_MIN_W = 230;
    private static final int CARD_H = 140;
    private static final int CARD_GAP = 16;
    private static final int CONTENT_PAD = 24;
    private static final int SCROLLBAR_W = 5;

    // ==================== STATE ====================
    private String searchText = "";
    private boolean searchFocused = false;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private Module bindingModule = null;
    private Module expandedModule = null;
    private Module.Setting<?> draggingSetting = null;
    private int activeCategory = 0;
    private long openTime;

    // ==================== ANIMATION ====================
    private final Map<Module, Float> hoverAnim = new HashMap<>();
    private final Map<Module, Float> switchAnim = new HashMap<>();
    private float scrollAnim = 0;
    private boolean scrollbarHover = false;

    // ==================== LAYOUT DATA ====================
    private final List<CardRect> cardRects = new ArrayList<>();
    private final List<CatBtn> catBtns = new ArrayList<>();
    private final Map<Module.Setting<?>, int[]> sliderRects = new HashMap<>();

    // ==================== CATEGORIES ====================
    private static final String[] CAT_ICONS = {"\u26a1", "\u2694", "\u25c9", "\u25ba", "\u2666", "\u25a3", "\u2726"};
    private static final String[] CAT_NAMES = {"Todos", "Combat", "Render", "Movement", "Player", "HUD", "Misc"};
    private static final Category[] CAT_VAL = {null, Category.COMBAT, Category.RENDER, Category.MOVEMENT, Category.PLAYER, Category.HUD, Category.MISC};

    // ==================== INIT ====================

    @Override
    public void initGui() {
        super.initGui();
        openTime = System.currentTimeMillis();
        scrollOffset = 0;
        scrollAnim = 0;
        bindingModule = null;
        expandedModule = null;
        draggingSetting = null;
        hoverAnim.clear();
        searchFocused = false;

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        panelW = Math.max(600, Math.min(1050, (int) (sw * 0.92f)));
        panelH = Math.max(400, Math.min(680, (int) (sh * 0.88f)));
        panelX = (sw - panelW) / 2;
        panelY = (sh - panelH) / 2;

        headerY = panelY;
        headerH = HEADER_H;

        sidebarX = panelX;
        sidebarY = panelY + HEADER_H;
        sidebarW = Math.max(160, Math.min(SIDEBAR_W, panelW / 5));
        sidebarH = panelH - HEADER_H - FOOTER_H;

        contentX = panelX + sidebarW;
        contentY = panelY + HEADER_H;
        contentW = panelW - sidebarW;
        contentH = panelH - HEADER_H - FOOTER_H;

        footerY = panelY + panelH - FOOTER_H;
        footerH = FOOTER_H;

        searchW = Math.min(320, contentW / 3);
        searchH = 22;
        searchX = panelX + panelW - searchW - 28;
        searchY = panelY + (HEADER_H - searchH) / 2;

        buildCatBtns();
    }

    // ==================== DRAW ====================

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        float t = Math.min(1.0f, (System.currentTimeMillis() - openTime) / 300.0f);
        float eased = 1.0f - (float) Math.pow(1.0 - t, 3);

        buildLayout(mouseX, mouseY);

        scrollAnim += (scrollOffset - scrollAnim) * 0.25f;
        if (Math.abs(scrollAnim - scrollOffset) < 0.5f) scrollAnim = scrollOffset;

        // Background darken
        drawRect(0, 0, this.width, this.height, 0xCC000000);

        GlStateManager.pushMatrix();

        // Fade-in scale
        if (eased < 1.0f) {
            float cx = panelX + panelW / 2.0f;
            float cy = panelY + panelH / 2.0f;
            float s = 0.96f + 0.04f * eased;
            GlStateManager.translate(cx, cy, 0);
            GlStateManager.scale(s, s, 1);
            GlStateManager.translate(-cx, -cy, 0);
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);

        drawPanelBg();
        drawHeader(mouseX, mouseY);
        drawSidebar(mouseX, mouseY);
        drawContent(mouseX, mouseY);
        drawFooter(mouseX, mouseY);
        drawScrollbar();

        GlStateManager.disableBlend();

        if (bindingModule != null) drawBindingOverlay();

        GlStateManager.popMatrix();
    }

    // ==================== PANEL ====================

    private void drawPanelBg() {
        // Shadow
        drawRoundedRect(panelX + 4, panelY + 8, panelW, panelH, 18, 0x30000000);
        drawRoundedRect(panelX + 2, panelY + 4, panelW + 2, panelH + 1, 16, 0x18000000);
        // Main bg
        drawRoundedRect(panelX, panelY, panelW, panelH, 16, PANEL_BG);
        // Border
        drawRoundedRect(panelX, panelY, panelW, panelH, 16, PANEL_BORDER);
        // Inset top highlight
        drawRect(panelX + 20, panelY + 1, panelX + panelW - 20, panelY + 2, PANEL_INNER);
    }

    // ==================== HEADER ====================

    private void drawHeader(int mx, int my) {
        // Header bg
        drawRect(panelX + 1, headerY + HEADER_H - 1, panelX + panelW - 1, headerY + HEADER_H, HEADER_BORDER);

        // Brand icon (gradient square + logo)
        int iconS = 36;
        int iconX = panelX + 26;
        int iconY = headerY + (HEADER_H - iconS) / 2;
        drawRoundedRect(iconX, iconY, iconS, iconS, 10, C_INDIGO);
        // Logo on top
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1, 1, 1, 1);
        if (LOGO_TEXTURE > 0) {
            TextureLoader.bind(LOGO_TEXTURE);
        }
        drawModalRectWithCustomSizedTexture(iconX + 3, iconY + 3, 0, 0, iconS - 6, iconS - 6, 512, 512);
        GlStateManager.disableBlend();

        // Brand text
        mc.fontRendererObj.drawStringWithShadow("\u00a7lBee Client", iconX + iconS + 12, headerY + 15, C_TEXT);
        mc.fontRendererObj.drawStringWithShadow("Forge 1.8.9 \u00b7 v" + com.beeclient.BeeClient.VERSION, iconX + iconS + 12, headerY + 31, C_MUTED);

        // Search box
        boolean focus = searchFocused;
        int bg = focus ? SEARCH_FOCUS_BG : SEARCH_BG;
        int border = focus ? SEARCH_FOCUS_BORDER : SEARCH_BORDER;
        drawRoundedRect(searchX, searchY, searchW, searchH, 8, bg);
        drawRoundedRect(searchX, searchY, searchW, searchH, 8, border);

        // Search icon (circle + line)
        drawSearchIcon(searchX + 14, searchY + searchH / 2);

        // Search text
        String display = searchText.isEmpty() ? "Pesquisar mods..." : searchText;
        int tc = searchText.isEmpty() ? C_MUTED : C_TEXT;
        mc.fontRendererObj.drawStringWithShadow(display, searchX + 30, searchY + 6, tc);

        // Blinking cursor
        if (searchFocused && (System.currentTimeMillis() / 530) % 2 == 0) {
            int cx = searchX + 30 + mc.fontRendererObj.getStringWidth(searchText) + 2;
            drawRect(cx, searchY + 5, cx + 1, searchY + searchH - 5, C_INDIGO);
        }
    }

    private void drawSearchIcon(float cx, float cy) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(0.545f, 0.580f, 0.620f, 1.0f);
        GL11.glLineWidth(1.5f);
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        // Circle
        wr.begin(2, DefaultVertexFormats.POSITION);
        for (int i = 0; i <= 36; i++) {
            float r = (float) Math.toRadians(i * 10);
            wr.pos(cx + Math.cos(r) * 4.5f, cy - 1 + Math.sin(r) * 4.5f, 0).endVertex();
        }
        t.draw();
        // Handle
        wr.begin(1, DefaultVertexFormats.POSITION);
        wr.pos(cx + 3, cy + 3, 0).endVertex();
        wr.pos(cx + 7.5f, cy + 7.5f, 0).endVertex();
        t.draw();
        GL11.glLineWidth(1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    // ==================== SIDEBAR ====================

    private void buildCatBtns() {
        catBtns.clear();
        int bw = sidebarW - 16;
        int bh = 36;
        int bx = sidebarX + 8;
        int by = sidebarY + 16;
        for (int i = 0; i < CAT_NAMES.length; i++) {
            catBtns.add(new CatBtn(i, bx, by + i * (bh + 6), bw, bh));
        }
    }

    private void drawSidebar(int mx, int my) {
        // Bg
        drawRect(sidebarX, sidebarY, sidebarX + sidebarW, sidebarY + sidebarH, SIDEBAR_BG);
        // Border right
        drawRect(sidebarX + sidebarW - 1, sidebarY, sidebarX + sidebarW, sidebarY + sidebarH, SIDEBAR_BORDER);

        for (CatBtn btn : catBtns) {
            boolean hover = mx >= btn.x && mx <= btn.x + btn.w && my >= btn.y && my <= btn.y + btn.h;
            boolean active = btn.idx == activeCategory;

            if (active) {
                // Gradient bg
                drawGradientH(btn.x, btn.x + btn.w, btn.y, btn.y + btn.h, NAV_ACTIVE_GRADIENT_L, NAV_ACTIVE_GRADIENT_R);
                // Left border
                drawRect(btn.x, btn.y + 4, btn.x + 3, btn.y + btn.h - 4, C_INDIGO);
            } else if (hover) {
                drawRect(btn.x, btn.y, btn.x + btn.w, btn.y + btn.h, NAV_HOVER);
            }

            int tc = active ? C_INDIGO_LIGHT : (hover ? C_TEXT : C_MUTED);
            String label = CAT_ICONS[btn.idx] + "  " + CAT_NAMES[btn.idx];
            mc.fontRendererObj.drawStringWithShadow(label, btn.x + 14, btn.y + 12, tc);
        }
    }

    // ==================== CONTENT ====================

    private void drawContent(int mx, int my) {
        // Clip
        ScaledResolution sr = new ScaledResolution(mc);
        int scale = sr.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(
                contentX * scale,
                mc.displayHeight - (contentY + contentH) * scale,
                contentW * scale,
                contentH * scale
        );

        // Bg
        drawRect(contentX, contentY, contentX + contentW, contentY + contentH, SIDEBAR_BG);

        for (CardRect cr : cardRects) {
            drawCard(cr, mx, my);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawCard(CardRect cr, int mx, int my) {
        Module m = cr.mod;
        boolean on = m.isEnabled();
        float hov = hoverAnim.getOrDefault(m, 0.0f);
        boolean expanded = expandedModule == m;

        int drawY = cr.y - (int) (hov * 2);

        // Card bg
        int bg = lerpColor(CARD_BG, CARD_HOVER_BG, hov);
        if (on) bg = lerpColor(bg, CARD_ACTIVE_BG, 0.6f);
        drawRoundedRect(cr.x, drawY, cr.w, cr.h, 12, bg);

        // Border
        int bd = lerpColor(CARD_BORDER, CARD_HOVER_BORDER, hov);
        if (on) bd = lerpColor(bd, CARD_ACTIVE_BORDER, 0.7f);
        drawRoundedRect(cr.x, drawY, cr.w, cr.h, 12, bd);

        // Name
        mc.fontRendererObj.drawStringWithShadow(m.getName(), cr.x + 16, drawY + 16, on ? C_TEXT : C_MUTED);

        // Description (2 lines)
        String desc = m.getDescription();
        if (desc != null && !desc.isEmpty()) {
            List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(desc, cr.w - 32);
            for (int i = 0; i < Math.min(lines.size(), 2); i++) {
                mc.fontRendererObj.drawStringWithShadow(lines.get(i), cr.x + 16, drawY + 34 + i * 12, 0xCC8B949E);
            }
        }

        // Footer
        int fy = drawY + cr.h - 36;

        // Settings button (only if has settings)
        if (!m.getSettings().isEmpty()) {
            boolean gHov = mx >= cr.gearX && mx <= cr.gearX + 28 && my >= fy && my <= fy + 28;
            int gBg = gHov ? 0x26FFFFFF : 0x0DFFFFFF;
            drawRoundedRect(cr.gearX, fy, 28, 28, 6, gBg);
            drawRoundedRect(cr.gearX, fy, 28, 28, 6, 0x14FFFFFF);
            // Gear icon (3 dots)
            int dotColor = gHov ? C_TEXT : C_MUTED;
            drawRoundedRect(cr.gearX + 11, fy + 6, 2, 2, 1, dotColor);
            drawRoundedRect(cr.gearX + 11, fy + 13, 2, 2, 1, dotColor);
            drawRoundedRect(cr.gearX + 11, fy + 20, 2, 2, 1, dotColor);
        }

        // Keybind chip
        if (m.getKeyBind() != 0 || bindingModule == m) {
            String keyText = bindingModule == m ? "..." : Keyboard.getKeyName(m.getKeyBind());
            String chipLabel = "[" + keyText + "]";
            int chipW = mc.fontRendererObj.getStringWidth(chipLabel) + 8;
            int chipX = cr.switchX - chipW - 8;
            int chipBg = (bindingModule == m) ? 0x336366F1 : 0x0DFFFFFF;
            drawRoundedRect(chipX, fy + 4, chipW, 20, 5, chipBg);
            int chipTc = (bindingModule == m) ? C_INDIGO_LIGHT : 0xFF9FB0D8;
            mc.fontRendererObj.drawStringWithShadow(chipLabel, chipX + 4, fy + 8, chipTc);
        }

        // Switch
        drawSwitch(cr.switchX, fy + 4, on);

        // Expanded settings
        if (expanded && !m.getSettings().isEmpty()) {
            drawSettings(cr, drawY + cr.h + 4, mx, my);
        }
    }

    private void drawSwitch(int x, int y, boolean on) {
        int w = 38, h = 20;
        float anim = switchAnim.getOrDefault(null, 0.0f);

        // Track
        drawRoundedRect(x, y, w, h, 10, on ? SW_ON : SW_OFF);

        // Knob
        float knobX = on ? x + 21 : x + 3;
        // Glow when on
        if (on) {
            drawRoundedRect(knobX - 1, y + 2, 16, 16, 8, 0x40FFFFFF);
        }
        drawRoundedRect(knobX, y + 3, 14, 14, 7, on ? C_WHITE : KNOB_OFF);
    }

    private void drawSettings(CardRect cr, int sy, int mx, int my) {
        Module m = cr.mod;
        int pw = cr.w;
        int ph = 12;
        for (Module.Setting<?> s : m.getSettings()) {
            ph += s.isBoolean() ? 22 : 34;
        }

        drawRoundedRect(cr.x, sy, pw, ph, 10, 0xE0161B22);
        drawRect(cr.x + 1, sy + 1, cr.x + pw - 1, sy + 2, 0x14FFFFFF);

        int ry = sy + 10;
        int ix = cr.x + 16;
        int iw = pw - 32;

        for (Module.Setting<?> s : m.getSettings()) {
            if (s.isBoolean()) {
                mc.fontRendererObj.drawStringWithShadow(s.getName(), ix, ry + 3, C_TEXT);
                boolean val = s.getBoolean();
                int swX = cr.x + pw - 54;
                drawSwitch(swX, ry, val);
                ry += 22;
            } else if (s.isNumber()) {
                mc.fontRendererObj.drawStringWithShadow(s.getName(), ix, ry, C_TEXT);
                double v = s.getNumber().doubleValue();
                String valText = s.getNumber() instanceof Integer || s.getNumber() instanceof Long
                        ? String.valueOf((long) v)
                        : String.format(java.util.Locale.US, "%.1f", v);
                mc.fontRendererObj.drawStringWithShadow(valText, ix + iw - mc.fontRendererObj.getStringWidth(valText), ry, C_INDIGO_LIGHT);

                int tY = ry + 14, tH = 4;
                double t = s.normalized(s.getNumber());
                drawRoundedRect(ix, tY, iw, tH, 2, 0x338B949E);
                int fill = (int) (iw * t);
                if (fill > 2) drawRoundedRect(ix, tY, fill, tH, 2, C_INDIGO);
                int kx = Math.max(ix, Math.min(ix + iw - 8, ix + fill - 4));
                drawRoundedRect(kx, tY - 3, 8, 10, 4, C_WHITE);
                sliderRects.put(s, new int[]{ix, tY - 4, iw, tH + 8});
                ry += 34;
            }
        }
    }

    // ==================== FOOTER ====================

    private void drawFooter(int mx, int my) {
        // Bg
        drawRect(panelX + 1, footerY, panelX + panelW - 1, footerY + footerH, FOOTER_BG);
        drawRect(panelX + 1, footerY, panelX + panelW - 1, footerY + 1, FOOTER_BORDER);

        // Status dot
        int dotX = panelX + 28;
        int dotY = footerY + footerH / 2 - 4;
        drawRoundedRect(dotX, dotY, 8, 8, 4, C_GREEN);

        // Status text
        int active = 0;
        for (Module m : ModuleManager.getInstance().getModules()) if (m.isEnabled()) active++;
        String status = active + " Mods Ativos \u00b7 Pronto para Jogar";
        mc.fontRendererObj.drawStringWithShadow(status, dotX + 16, footerY + footerH / 2 - 4, C_MUTED);

        // Buttons
        int btnH = 26;
        int btnY = footerY + (FOOTER_H - btnH) / 2;

        // "Resetar Padr\u00f5es" (secondary)
        String resetTxt = "Resetar Padr\u00f5es";
        int resetW = mc.fontRendererObj.getStringWidth(resetTxt) + 22;
        int resetX = panelX + panelW - 16 - resetW - 12 - mc.fontRendererObj.getStringWidth("Fechar (ESC)") - 22;
        boolean rHov = mx >= resetX && mx <= resetX + resetW && my >= btnY && my <= btnY + btnH;
        drawRoundedRect(resetX, btnY, resetW, btnH, 8, rHov ? 0x1AFFFFFF : 0x0DFFFFFF);
        drawRoundedRect(resetX, btnY, resetW, btnH, 8, 0x14FFFFFF);
        mc.fontRendererObj.drawStringWithShadow(resetTxt, resetX + 11, btnY + 8, rHov ? C_TEXT : 0xFFC9D1D9);

        // "Fechar (ESC)" (primary)
        String closeTxt = "Fechar (ESC)";
        int closeW = mc.fontRendererObj.getStringWidth(closeTxt) + 22;
        int closeX = panelX + panelW - 16 - closeW;
        boolean cHov = mx >= closeX && mx <= closeX + closeW && my >= btnY && my <= btnY + btnH;
        drawRoundedRect(closeX, btnY, closeW, btnH, 8, cHov ? C_INDIGO : C_INDIGO_DARK);
        mc.fontRendererObj.drawStringWithShadow(closeTxt, closeX + 11, btnY + 8, C_TEXT);
    }

    // ==================== SCROLLBAR ====================

    private void drawScrollbar() {
        if (maxScroll <= 0) return;
        int areaH = contentH;
        int total = maxScroll + areaH;
        int thumbH = Math.max(24, (int) (areaH * ((float) areaH / total)));
        int thumbY = contentY + (int) (scrollAnim / (float) maxScroll * (areaH - thumbH));
        int thumbColor = scrollbarHover ? SCROLL_THUMB_HOVER : SCROLL_THUMB;
        drawRoundedRect(contentX + contentW - SCROLLBAR_W - 4, thumbY, SCROLLBAR_W, thumbH, 3, thumbColor);
    }

    // ==================== BINDING OVERLAY ====================

    private void drawBindingOverlay() {
        drawRect(0, 0, this.width, this.height, 0x60000000);

        String t1 = "Pressione uma tecla para vincular a " + bindingModule.getName();
        String t2 = "ESC / DEL remove a tecla";
        int w = Math.max(mc.fontRendererObj.getStringWidth(t1), mc.fontRendererObj.getStringWidth(t2)) + 44;
        int h = 64;
        int x = this.width / 2 - w / 2;
        int y = this.height / 2 - h / 2;

        drawRoundedRect(x - 4, y - 4, w + 8, h + 8, 16, 0x40000000);
        drawRoundedRect(x, y, w, h, 12, 0xF0161B22);
        drawRoundedRect(x, y, w, h, 12, 0x406366F1);
        mc.fontRendererObj.drawStringWithShadow(t1, x + 22, y + 18, C_TEXT);
        mc.fontRendererObj.drawStringWithShadow(t2, x + 22, y + 40, C_MUTED);
    }

    // ==================== LAYOUT ====================

    private void buildLayout(int mx, int my) {
        cardRects.clear();
        sliderRects.clear();

        Category filterCat = (activeCategory > 0 && activeCategory < CAT_VAL.length) ? CAT_VAL[activeCategory] : null;

        int innerX = contentX + CONTENT_PAD;
        int innerW = contentW - CONTENT_PAD * 2 - SCROLLBAR_W - 8;
        int cols = Math.max(1, (innerW + CARD_GAP) / (CARD_MIN_W + CARD_GAP));
        int cardW = (innerW - (cols - 1) * CARD_GAP) / cols;
        int curX = innerX;
        int curY = contentY + CONTENT_PAD - scrollOffset;
        int col = 0;

        for (Module m : ModuleManager.getInstance().getModules()) {
            if (filterCat != null && m.getCategory() != filterCat) continue;
            if (!searchText.isEmpty()) {
                String q = searchText.toLowerCase();
                if (!m.getName().toLowerCase().contains(q) && (m.getDescription() == null || !m.getDescription().toLowerCase().contains(q))) continue;
            }

            if (col >= cols) {
                col = 0;
                curX = innerX;
                curY += CARD_H + CARD_GAP;
            }

            int gearX = curX + 16;
            int switchX = curX + cardW - 54;

            cardRects.add(new CardRect(m, curX, curY, cardW, CARD_H, gearX, switchX));

            // Hover animation
            int drawY = curY - (int) (hoverAnim.getOrDefault(m, 0.0f) * 2);
            boolean hov = mx >= curX && mx <= curX + cardW && my >= drawY && my <= drawY + CARD_H;
            float tgt = hov ? 1.0f : 0.0f;
            float cur = hoverAnim.getOrDefault(m, 0.0f);
            cur += (tgt - cur) * 0.25f;
            if (Math.abs(cur - tgt) < 0.01f) cur = tgt;
            hoverAnim.put(m, cur);

            // Switch animation
            float sTgt = m.isEnabled() ? 1.0f : 0.0f;
            float sCur = switchAnim.getOrDefault(m, sTgt);
            sCur += (sTgt - sCur) * 0.2f;
            if (Math.abs(sCur - sTgt) < 0.01f) sCur = sTgt;
            switchAnim.put(m, sCur);

            curX += cardW + CARD_GAP;
            col++;

            if (m == expandedModule && !m.getSettings().isEmpty()) {
                int eh = 12;
                for (Module.Setting<?> s : m.getSettings()) eh += s.isBoolean() ? 22 : 34;
                curY += CARD_H + CARD_GAP + eh;
            }
        }

        // MaxScroll: total content height - visible height
        int totalContent = (curY + scrollOffset) - contentY + CONTENT_PAD + CARD_GAP;
        maxScroll = Math.max(0, totalContent - contentH);
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        if (scrollOffset < 0) scrollOffset = 0;

        // Scrollbar hover
        scrollbarHover = maxScroll > 0 && mx >= contentX + contentW - SCROLLBAR_W - 8 && mx <= contentX + contentW && my >= contentY && my <= contentY + contentH;
    }

    // ==================== INPUT ====================

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int d = Mouse.getEventDWheel();
        if (d != 0) {
            scrollOffset -= (d > 0 ? 24 : -24);
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (bindingModule != null) {
            bindingModule = null;
            return;
        }

        // Search focus
        if (mouseX >= searchX && mouseX <= searchX + searchW && mouseY >= searchY && mouseY <= searchY + searchH) {
            searchFocused = true;
            return;
        }
        searchFocused = false;

        // Sidebar
        for (CatBtn btn : catBtns) {
            if (mouseX >= btn.x && mouseX <= btn.x + btn.w && mouseY >= btn.y && mouseY <= btn.y + btn.h) {
                activeCategory = btn.idx;
                scrollOffset = 0;
                scrollAnim = 0;
                return;
            }
        }

        // Footer buttons
        int btnH = 26;
        int btnY2 = footerY + (FOOTER_H - btnH) / 2;
        String closeTxt = "Fechar (ESC)";
        int closeW = mc.fontRendererObj.getStringWidth(closeTxt) + 22;
        int closeX = panelX + panelW - 16 - closeW;
        if (mouseX >= closeX && mouseX <= closeX + closeW && mouseY >= btnY2 && mouseY <= btnY2 + btnH) {
            mc.displayGuiScreen(null);
            return;
        }
        String resetTxt = "Resetar Padr\u00f5es";
        int resetW = mc.fontRendererObj.getStringWidth(resetTxt) + 22;
        int resetX = closeX - 12 - resetW;
        if (mouseX >= resetX && mouseX <= resetX + resetW && mouseY >= btnY2 && mouseY <= btnY2 + btnH) {
            for (Module m : ModuleManager.getInstance().getModules()) if (m.isEnabled()) m.toggle();
            return;
        }

        // Cards
        for (CardRect cr : cardRects) {
            int drawY = cr.y - (int) (hoverAnim.getOrDefault(cr.mod, 0.0f) * 2);
            if (mouseX < cr.x || mouseX > cr.x + cr.w || mouseY < drawY || mouseY > drawY + cr.h) continue;

            Module m = cr.mod;
            int fy = drawY + cr.h - 36;

            // Settings gear
            if (!m.getSettings().isEmpty() && mouseX >= cr.gearX && mouseX <= cr.gearX + 28 && mouseY >= fy && mouseY <= fy + 28) {
                expandedModule = (expandedModule == m) ? null : m;
                return;
            }

            // Keybind chip
            if (m.getKeyBind() != 0 || bindingModule == m) {
                String keyText = bindingModule == m ? "..." : Keyboard.getKeyName(m.getKeyBind());
                int chipW = mc.fontRendererObj.getStringWidth("[" + keyText + "]") + 8;
                int chipX = cr.switchX - chipW - 8;
                if (mouseX >= chipX && mouseX <= chipX + chipW && mouseY >= fy + 4 && mouseY <= fy + 24) {
                    if (mouseButton == 0) { m.setKeyBind(0); return; }
                }
            }

            // Switch
            if (mouseX >= cr.switchX && mouseX <= cr.switchX + 38 && mouseY >= fy + 4 && mouseY <= fy + 24) {
                m.toggle();
                return;
            }

            // Settings sliders
            if (expandedModule == m) {
                for (Map.Entry<Module.Setting<?>, int[]> e : sliderRects.entrySet()) {
                    int[] r = e.getValue();
                    if (mouseX >= r[0] && mouseX <= r[0] + r[2] && mouseY >= r[1] && mouseY <= r[1] + r[3]) {
                        draggingSetting = e.getKey();
                        updateSlider(draggingSetting, mouseX);
                        return;
                    }
                }
            }

            // Card body
            if (mouseButton == 0) m.toggle();
            else if (mouseButton == 1) bindingModule = m;
            return;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (clickedMouseButton == 0 && draggingSetting != null) updateSlider(draggingSetting, mouseX);
    }

    private void updateSlider(Module.Setting<?> s, int mouseX) {
        int[] r = sliderRects.get(s);
        if (r == null) return;
        double t = Math.max(0, Math.min(1, (double) (mouseX - r[0]) / r[2]));
        s.setNumber(s.denormalized(t));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        draggingSetting = null;
        com.beeclient.config.ConfigManager.INSTANCE.save();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (bindingModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                bindingModule.setKeyBind(0);
            } else if (keyCode != Keyboard.KEY_RSHIFT && keyCode != Keyboard.KEY_LSHIFT) {
                bindingModule.setKeyBind(keyCode);
            }
            bindingModule = null;
            return;
        }

        if (searchFocused) {
            if (keyCode == Keyboard.KEY_BACK && !searchText.isEmpty()) {
                searchText = searchText.substring(0, searchText.length() - 1);
            } else if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
                searchFocused = false;
            } else if (typedChar != 0 && typedChar != 27 && searchText.length() < 40) {
                searchText += typedChar;
            }
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(null);
        else if (keyCode == Keyboard.KEY_TAB) searchFocused = true;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // ==================== HELPERS ====================

    private static void drawRoundedRect(float x, float y, float w, float h, float r, int c) {
        RenderUtils.drawRoundedRect(x, y, w, h, r, c);
    }

    private static void drawGradientH(int l, int r, int t, int b, int c1, int c2) {
        RenderUtils.drawGradientH(l, r, t, b, c1, c2);
    }

    private static int lerpColor(int a, int b, float t) {
        int aA = (a >> 24) & 0xFF, aR = (a >> 16) & 0xFF, aG = (a >> 8) & 0xFF, aB = a & 0xFF;
        int bA = (b >> 24) & 0xFF, bR = (b >> 16) & 0xFF, bG = (b >> 8) & 0xFF, bB = b & 0xFF;
        return ((int) (aA + (bA - aA) * t) << 24) | ((int) (aR + (bR - aR) * t) << 16)
                | ((int) (aG + (bG - aG) * t) << 8) | (int) (aB + (bB - aB) * t);
    }

    // ==================== INNER ====================

    static class CardRect {
        Module mod;
        int x, y, w, h, gearX, switchX;
        CardRect(Module m, int x, int y, int w, int h, int gx, int sx) {
            mod = m; this.x = x; this.y = y; this.w = w; this.h = h; gearX = gx; switchX = sx;
        }
    }

    static class CatBtn {
        int idx, x, y, w, h;
        CatBtn(int i, int x, int y, int w, int h) {
            idx = i; this.x = x; this.y = y; this.w = w; this.h = h;
        }
    }
}
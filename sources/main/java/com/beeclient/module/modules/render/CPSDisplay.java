package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import com.beeclient.render.RenderUtils;
import com.beeclient.util.ClickCounter;
import net.minecraft.client.gui.ScaledResolution;

public class CPSDisplay extends Module {

    public CPSDisplay() {
        super("CPSDisplay", "Displays clicks per second", Category.RENDER, 0);
    }

    @Override
    public void onRender() {
        ScaledResolution sr = new ScaledResolution(mc);
        String text = "\u00a7eCPS \u00a77\u00bb \u00a7f" + ClickCounter.INSTANCE.getLmbCps()
                + " \u00a77| \u00a7f" + ClickCounter.INSTANCE.getRmbCps();
        RenderUtils.drawString(text, 4, 70, 0xFFEAEAEA);
    }
}
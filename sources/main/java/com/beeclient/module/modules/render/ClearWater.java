package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;

public class ClearWater extends Module {

    private float oldGamma;

    public ClearWater() {
        super("ClearWater", "Increases underwater visibility", Category.RENDER, 0);
    }

    @Override
    protected void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100.0f;
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}
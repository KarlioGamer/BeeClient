package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;

public class NoHurtCam extends Module {

    public NoHurtCam() {
        super("NoHurtCam", "Removes camera shake when hit", Category.RENDER, 0);
    }

    @Override
    public void onTick() {
        if (mc.thePlayer != null) {
            mc.thePlayer.hurtTime = 0;
        }
    }
}
package com.beeclient.module.modules.render;

import com.beeclient.module.Category;
import com.beeclient.module.Module;
import net.minecraft.world.World;

public class TimeChanger extends Module {

    public TimeChanger() {
        super("TimeChanger", "Changes visual time of day", Category.RENDER, 0);
        addSetting(new Setting<>("Time", 6000, 0, 24000));
    }

    @Override
    public void onTick() {
        if (mc.theWorld != null) {
            mc.theWorld.setWorldTime(getTime());
        }
    }

    public int getTime() {
        Setting<?> s = getSetting("Time");
        if (s != null && s.isNumber()) {
            return s.getNumber().intValue();
        }
        return 6000;
    }
}
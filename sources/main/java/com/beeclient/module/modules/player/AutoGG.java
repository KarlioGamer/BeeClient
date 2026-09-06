package com.beeclient.module.modules.player;

import com.beeclient.event.events.ChatEvent;
import com.beeclient.module.Category;
import com.beeclient.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoGG extends Module {

    public AutoGG() {
        super("AutoGG", "Automatically sends 'gg' at game end", Category.PLAYER, 0);
    }

    @Override
    public void onChat(String message) {
        if (message.toLowerCase().contains("game over") ||
            message.toLowerCase().contains("winner") ||
            message.toLowerCase().contains("victory")) {
            mc.thePlayer.sendChatMessage("gg");
        }
    }
}

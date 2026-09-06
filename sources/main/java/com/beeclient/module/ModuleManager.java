package com.beeclient.module;

import com.beeclient.BeeClient;
import com.beeclient.event.Event;
import com.beeclient.module.modules.combat.*;
import com.beeclient.module.modules.render.*;
import com.beeclient.module.modules.movement.*;
import com.beeclient.module.modules.player.*;
import com.beeclient.module.modules.hud.*;
import com.beeclient.module.modules.misc.*;
import com.beeclient.util.ClickCounter;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {

    private static ModuleManager instance;
    private List<Module> modules = new CopyOnWriteArrayList<>();

    public ModuleManager() {
        instance = this;
    }

    public static ModuleManager getInstance() {
        return instance;
    }

    public void init() {
        // Combat
        modules.add(new HitDelayFix());
        modules.add(new ReachDisplay());

        // Render
        modules.add(new Keystrokes());
        modules.add(new ArmorHUD());
        modules.add(new PotionHUD());
        modules.add(new FPSDisplay());
        modules.add(new CPSDisplay());
        modules.add(new CrosshairMod());
        modules.add(new Fullbright());
        modules.add(new TimeChanger());
        modules.add(new ChromaHUD());
        modules.add(new PerspectiveMod());
        modules.add(new ItemPhysicLite());
        modules.add(new BlockOverlay());
        modules.add(new BossbarCustomizer());
        modules.add(new GlintColorizer());
        modules.add(new ParticleCustomizer());
        modules.add(new ClearWater());
        modules.add(new ShinyPots());
        modules.add(new DamageIndicators());
        modules.add(new ItemBlur());
        modules.add(new DirectionHUD());
        modules.add(new ScrollableTooltips());
        modules.add(new NoHurtCam());

        // Movement
        modules.add(new ToggleSprint());
        modules.add(new ToggleSneak());
        modules.add(new MouseDelayFix());

        // Player
        modules.add(new CoordinatesHUD());
        modules.add(new PingDisplay());
        modules.add(new NameHistory());
        modules.add(new LevelHead());
        modules.add(new TNTTime());
        modules.add(new AutoGG());
        modules.add(new Quickplay());
        modules.add(new CompactChat());
        modules.add(new ChatTriggers());
        modules.add(new AutoReconnect());

        // HUD
        modules.add(new SidebarRevamp());
        modules.add(new BetterThirdPerson());
        modules.add(new Watermark());
        modules.add(new ArrayListHUD());

        // Misc
        modules.add(new DiscordRP());
        modules.add(new TCPNoDelay());
        modules.add(new InputLagFix());
        modules.add(new SoundPhysics());
        modules.add(new VanillaEnhancements());
        modules.add(new ReplayMod());

        System.out.println("[Bee Client] Loaded " + modules.size() + " modules.");
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) {
                result.add(m);
            }
        }
        return result;
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public void onTick() {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }

    public void onRender() {
        ClickCounter.INSTANCE.update();
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onRender();
            }
        }
    }

    public void onRenderWorld(float partialTicks) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onRenderWorld(partialTicks);
            }
        }
    }

    public void onKeyInput() {
        for (Module m : modules) {
            if (m.getKeyBind() != 0 && Keyboard.isKeyDown(m.getKeyBind()) && !Keyboard.getEventKeyState()) {
                m.toggle();
            }
        }
    }

    public void onChat(String message) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onChat(message);
            }
        }
    }
}

# Bee Client

Utility client for **Minecraft 1.8.9 (Forge 11.15.1.2318)**. Focused on quality-of-life and visual comfort — no modules that alter movement, reach, or packets sent to the server.

> Status: works on the production launcher (registered via ForgeCoreMod). All features are client-side and valid on servers with anti-cheat (GrimAC, etc.).

---

## 📦 Features

### Interface & HUD
- Full **ClickGUI** with search, category tabs, sliders and keybind rebinding (menu: `Right Shift`)
- Custom main menu (background texture + logo)
- Client brand on the HUD (top-right corner, like the "vanilla" tag)
- Customizable HUD: Keystrokes, CPS, FPS, Direction, Coordinates, Ping, Potions, Armor, ArrayList, Watermark
- Module states and settings **persist between sessions** (config file)

### Gameplay / QoL
- **ToggleSprint** and **ToggleSneak** — 100% vanilla behavior via physical key emulation (no simulation flag on anti-cheat)
- **NoHurtCam** — removes camera shake when taking damage (keeps the red flash)
- **Fullbright / ClearWater** — visibility without a potion effect on the server
- **Perspective** — 360° free camera
- **AutoGG**, **ChatTriggers**, **AutoReconnect**, **Quickplay**, **TCPNoDelay**
- **Discord Rich Presence** — in-game status on Discord (RPC)
- **ReachDisplay** — shows the distance of your last **real** hit (display only, measured against the bounding box, no reach change)

### Visual / Rendering
- TimeChanger, ItemPhysicLite, ItemBlur, GlintColorizer, ShinyPots, ParticleCustomizer, ChromaHUD, ScrollableTooltips, SidebarRevamp, BetterThirdPerson, BossbarCustomizer, DamageIndicators, BlockOverlay, CrosshairMod, LevelHead, TNTTime, NameHistory, Potion highlight

---

## 🗂 Modules

| Category | Modules |
|----------|---------|
| Combat   | HitDelayFix, ReachDisplay |
| Movement | ToggleSprint, ToggleSneak, MouseDelayFix |
| Render   | NoHurtCam, Fullbright, TimeChanger, ChromaHUD, Perspective, ItemPhysicLite, ItemBlur, BlockOverlay, BossbarCustomizer, GlintColorizer, ParticleCustomizer, ClearWater, ShinyPots, DamageIndicators, DirectionHUD, ScrollableTooltips, CrosshairMod, Keystrokes, ArmorHUD, PotionHUD, FPSDisplay, CPSDisplay |
| Player   | CoordinatesHUD, PingDisplay, NameHistory, LevelHead, TNTTime, AutoGG, Quickplay, CompactChat, ChatTriggers, AutoReconnect |
| HUD      | SidebarRevamp, BetterThirdPerson, Watermark, ArrayListHUD |
| Misc     | DiscordRP, TCPNoDelay, InputLagFix, SoundPhysics, VanillaEnhancements, ReplayMod |

### Default keys
| Key          | Action          |
|--------------|-----------------|
| `Right Shift`| Open the ClickGUI |
| `R`          | ToggleSprint    |
| `C`          | ToggleSneak     |
| `L`          | Perspective     |

---

## 🚀 Installation

1. Have **Minecraft 1.8.9 Forge 11.15.1.2318** installed in the launcher.
2. Copy `BeeClient-1.0.jar` to the mods folder:
   ```
   %APPDATA%\.minecraft\mods\BeeClient-1.0.jar
   ```
3. Launch the game and open the menu with `Right Shift`.

> Only the client jar is needed — the final file already bundles all dependencies (Discord RPC + SLF4J).

---

## 🔨 Build (for developers)

Requirements: **JDK 8** and **Gradle 4.5**.

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\jdk\jdk-8.0.302.8-hotspot\jdk8u302-b08"
gradle clean build
```

Output: `build/libs/BeeClient-1.0.jar` (single jar with bundled dependencies, reobfuscated).

---

## ⚙️ Configuration

Module state (enabled/disabled, keybinds and settings) is saved automatically to:

```
%APPDATA%\.minecraft\beeclient\config.json
```

- Saved when: toggling a module or keybind, releasing the mouse in the ClickGUI, and on game shutdown.
- Loaded at client init — nothing needs to be re-enabled between sessions.

---

## 🧩 Architecture

- **`com.beeclient.core.BeeClientLaunchPlugin`** — `IFMLLoadingPlugin` that registers the mod via `getModContainerClass()` (required because the launcher's `sklauncher-fx.jar` breaks `@Mod`-based discovery).
- **`com.beeclient.core.BeeClientModContainer`** — `DummyModContainer` providing `getSource()` to Forge (needed for resource packs).
- **`com.beeclient.render.TextureLoader`** — direct PNG loading from the classpath via `ImageIO` + `GlStateManager.bindTexture` (bypasses missing resource packs; avoids texture-cache desync that causes corrupted glyphs).
- **`com.beeclient.config.ConfigManager`** — JSON persistence via Gson (shipped with MC).
- **No Mixin** — the build does not include Mixin/SpongePowered (breaks on the launcher's classloader in production).

---

## ⚠️ Notes

- `HitDelayFix`, `MouseDelayFix` and other "stub" modules are QoL placeholders with no runtime effect.
- The mod does **not** extend reach or speed; any "simulation" flag on anti-cheat should be reported as a bug (ToggleSprint/Sneak use the vanilla physical key to avoid desync).
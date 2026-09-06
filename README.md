# Bee Client

Client utilitário para **Minecraft 1.8.9 (Forge 11.15.1.2318)**. Focado em melhorias de gameplay e conforto visual — sem módulos que alterem movimento, alcance ou pacotes enviados ao servidor.

> Status: funcional no launcher de produção (registro via ForgeCoreMod). Todas as features são client-side e válidas em servidores com anti-cheat (GrimAC, etc.).

---

## 📦 Funcionalidades

### Interface & HUD
- **ClickGUI** completo com busca, abas por categoria, sliders e bind de teclas (menu: `Right Shift`)
- Menu principal customizado (textura de fundo + logo)
- Brand do client no HUD (canto superior direito, como a marca "vanilla")
- HUD customizável: Keystrokes, CPS, FPS, Direção, Coordenadas, Ping, Potions, Armor, ArrayList, Watermark
- Posições e estados **persistem entre sessões** (arquivo de config)

### QoL / Jogabilidade
- **ToggleSprint** e **ToggleSneak** — comportamento 100% vanilla via emulação de tecla física (sem flag em anti-cheat de simulação)
- **NoHurtCam** — remove o tremor da câmera ao tomar dano (mantém o flash vermelho)
- **Fullbright / ClearWater** — visibilidade sem efeito de poção no servidor
- **Perspective** — câmera livre de 360°
- **AutoGG**, **ChatTriggers**, **AutoReconnect**, **Quickplay**, **TCPNoDelay**
- **Discord Rich Presence** — status do jogo no Discord (RPC)
- **ReachDisplay** — mostra a distância do último hit **real** (display apenas, medido contra o bounding box, sem alterar alcance)

### Visual / Render
- TimeChanger, ItemPhysicLite, ItemBlur, GlintColorizer, ShinyPots, ParticleCustomizer, ChromaHUD, ScrollableTooltips, SidebarRevamp, BetterThirdPerson, BossbarCustomizer, DamageIndicators, BlockOverlay, CrosshairMod, LevelHead, TNTTime, NameHistory, Colorido de poções

---

## 🗂 Módulos

| Categoria  | Módulos |
|------------|---------|
| Combat     | HitDelayFix, ReachDisplay |
| Movement   | ToggleSprint, ToggleSneak, MouseDelayFix |
| Render     | NoHurtCam, Fullbright, TimeChanger, ChromaHUD, Perspective, ItemPhysicLite, ItemBlur, BlockOverlay, BossbarCustomizer, GlintColorizer, ParticleCustomizer, ClearWater, ShinyPots, DamageIndicators, DirectionHUD, ScrollableTooltips, CrosshairMod, Keystrokes, ArmorHUD, PotionHUD, FPSDisplay, CPSDisplay |
| Player     | CoordinatesHUD, PingDisplay, NameHistory, LevelHead, TNTTime, AutoGG, Quickplay, CompactChat, ChatTriggers, AutoReconnect |
| HUD        | SidebarRevamp, BetterThirdPerson, Watermark, ArrayListHUD |
| Misc       | DiscordRP, TCPNoDelay, InputLagFix, SoundPhysics, VanillaEnhancements, ReplayMod |

### Teclas padrão
| Tecla        | Função                     |
|--------------|----------------------------|
| `Right Shift`| Abrir o ClickGUI           |
| `R`          | ToggleSprint               |
| `C`          | ToggleSneak                |
| `L`          | Perspective                |

---

## 🚀 Instalação

1. Tenha o Minecraft **1.8.9 Forge 11.15.1.2318** instalado no launcher.
2. Copie `BeeClient-1.0.jar` para a pasta de mods:
   ```
   %APPDATA%\.minecraft\mods\BeeClient-1.0.jar
   ```
3. Inicie o jogo e abra o menu com `Right Shift`.

> Apenas o client precisa do arquivo — o arquivo final já embute todas as dependências (Discord RPC + SLF4J).

---

## 🔨 Build (para desenvolvedores)

Requisitos: **JDK 8** e **Gradle 4.5**.

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\jdk\jdk-8.0.302.8-hotspot\jdk8u302-b08"
gradle clean build
```

Saída: `build/libs/BeeClient-1.0.jar` (jar único com dependências embutidas e reobfuscado).

---

## ⚙️ Configuração

O estado dos módulos (ativado/desativado, teclas e settings) é salvo automaticamente em:

```
%APPDATA%\.minecraft\beeclient\config.json
```

- Salvo ao: trocar estado/tecla de um módulo, soltar o mouse no ClickGUI e ao fechar o jogo.
- Carregado no init do client — não precisa reativar nada entre sessões.

---

## 🧩 Arquitetura

- **`com.beeclient.core.BeeClientLaunchPlugin`** — `IFMLLoadingPlugin` que registra o mod via `getModContainerClass()` (necessário porque o `sklauncher-fx.jar` do launcher quebra o discovery por `@Mod`).
- **`com.beeclient.core.BeeClientModContainer`** — `DummyModContainer` que fornece o `getSource()` ao Forge (necessário para resource packs).
- **`com.beeclient.render.TextureLoader`** — carregamento direto de PNGs do classpath via `ImageIO` + `GlStateManager.bindTexture` (contorna resource packs ausentes; evita dessync do cache de textura que causa glyphs corrompidos).
- **`com.beeclient.config.ConfigManager`** — persistência JSON via Gson (embutido no MC).
- **Sem Mixin** — o build não inclui Mixin/SpongePowered (quebra no classloader do launcher em produção).

---

## ⚠️ Notas

- `HitDelayFix`, `MouseDelayFix` e outros módulos "stub" são placeholders de QoL, sem efeito runtime.
- O mod **não** estende reach nem velocidade; any "simulation" flag em anti-cheat deve ser reportada como bug (o ToggleSprint/Sneak usam a tecla física do vanilla para evitar desync).
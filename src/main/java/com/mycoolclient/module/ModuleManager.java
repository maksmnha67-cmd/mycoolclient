package com.mycoolclient.module;

import com.mycoolclient.hud.modules.ClockHud;
import com.mycoolclient.hud.modules.CoordsHud;
import com.mycoolclient.hud.modules.FpsHud;
import com.mycoolclient.hud.modules.KeystrokesHud;
import com.mycoolclient.hud.modules.PotionTimersHud;
import com.mycoolclient.hud.modules.SelfNametagHud;
import com.mycoolclient.module.modules.AngelHaloModule;
import com.mycoolclient.module.modules.BlockOutlineModule;
import com.mycoolclient.module.modules.ChinaHatModule;
import com.mycoolclient.module.modules.HandVisualsModule;
import com.mycoolclient.module.modules.TargetHighlightModule;
import com.mycoolclient.module.modules.TrailModule;
import com.mycoolclient.module.modules.WingsModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Хранит все модули мода. Здесь только косметика/утилиты HUD —
 * никаких combat-фич, ESP, aura и т.д.
 */
public class ModuleManager {

    private static final List<Module> MODULES = new ArrayList<>();

    public static void init() {
        // --- HUD модули (стиль Rockstar Client: минималистичный, крупные акценты) ---
        register(new CoordsHud());
        register(new FpsHud());
        register(new ClockHud());
        register(new KeystrokesHud());
        register(new PotionTimersHud());
        register(new SelfNametagHud());

        // --- VISUAL модули: чисто косметика, без игрового преимущества ---
        register(new TrailModule());
        register(new HandVisualsModule());
        register(new TargetHighlightModule());
        register(new ChinaHatModule());
        register(new BlockOutlineModule());
        register(new AngelHaloModule());
        register(new WingsModule());
    }

    public static void register(Module module) {
        MODULES.add(module);
    }

    public static List<Module> getModules() {
        return MODULES;
    }

    public static List<Module> getByCategory(Module.Category category) {
        return MODULES.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public static Module getByName(String name) {
        for (Module m : MODULES) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}

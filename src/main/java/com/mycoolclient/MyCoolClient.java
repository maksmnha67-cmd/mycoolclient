package com.mycoolclient;

import com.mycoolclient.gui.ClickGuiScreen;
import com.mycoolclient.hud.HudRenderer;
import com.mycoolclient.module.Module;
import com.mycoolclient.module.ModuleManager;
import com.mycoolclient.module.modules.TargetHighlightModule;
import com.mycoolclient.module.modules.TrailModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MyCoolClient implements ClientModInitializer {

    private static HudRenderer hudRenderer;
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        hudRenderer = new HudRenderer();
        ModuleManager.init();

        // Правая Shift открывает GUI — как это принято во всех подобных клиентах
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mycoolclient.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.mycoolclient.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.openScreen(new ClickGuiScreen());
                }
            }

            // тик визуальных модулей
            for (Module module : ModuleManager.getByCategory(Module.Category.VISUAL)) {
                if (module instanceof TrailModule) {
                    ((TrailModule) module).tick();
                }
                if (module instanceof TargetHighlightModule) {
                    ((TargetHighlightModule) module).tick();
                }
            }
        });
    }

    public static HudRenderer getHudRenderer() {
        return hudRenderer;
    }
}

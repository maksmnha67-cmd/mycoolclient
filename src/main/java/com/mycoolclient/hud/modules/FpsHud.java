package com.mycoolclient.hud.modules;

import com.mycoolclient.hud.HudModule;
import net.minecraft.client.MinecraftClient;

/** Показывает текущий FPS. */
public class FpsHud extends HudModule {

    public FpsHud() {
        super("FPS", "Показывает количество кадров в секунду", 10, 34);
        setAccentColor(0xFF7CFF6B); // зелёный акцент
    }

    @Override
    public String getText() {
        int fps = MinecraftClient.getInstance().getCurrentFps();
        return "FPS: " + fps;
    }
}

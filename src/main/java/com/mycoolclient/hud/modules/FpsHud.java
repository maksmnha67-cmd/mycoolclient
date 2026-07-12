package com.mycoolclient.hud.modules;

import com.mycoolclient.hud.HudModule;

/** Показывает текущий FPS. */
public class FpsHud extends HudModule {

    // В 1.16.5 счётчик FPS внутри MinecraftClient приватный,
    // поэтому считаем кадры сами: getText() дергается раз за рендер-кадр.
    private int frameCount = 0;
    private int fps = 0;
    private long lastSampleTime = System.currentTimeMillis();

    public FpsHud() {
        super("FPS", "Показывает количество кадров в секунду", 10, 34);
        setAccentColor(0xFF7CFF6B); // зелёный акцент
    }

    @Override
    public String getText() {
        frameCount++;
        long now = System.currentTimeMillis();
        if (now - lastSampleTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastSampleTime = now;
        }
        return "FPS: " + fps;
    }
}

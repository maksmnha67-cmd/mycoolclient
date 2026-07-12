package com.mycoolclient.hud.modules;

import com.mycoolclient.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

/** Показывает координаты игрока — чисто информативно, как в ванильном F3, но красиво. */
public class CoordsHud extends HudModule {

    public CoordsHud() {
        super("Coords", "Показывает XYZ координаты игрока", 10, 10);
        setAccentColor(0xFF00D9FF); // голубой акцент
    }

    @Override
    public String getText() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Entity e = mc.getCameraEntity();
        if (e == null) return "XYZ: --";
        return String.format("XYZ: %.1f / %.1f / %.1f", e.getX(), e.getY(), e.getZ());
    }
}

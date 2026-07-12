package com.mycoolclient.hud.modules;

import com.mycoolclient.hud.HudModule;
import net.minecraft.client.MinecraftClient;

/** Показывает твой собственный ник в стильной капсуле (обычно свой ник не виден без F5). */
public class SelfNametagHud extends HudModule {

    public SelfNametagHud() {
        super("SelfNametag", "Показывает твой ник в углу экрана", 10, 180);
        setAccentColor(0xFFFFD24B); // золотой акцент
    }

    @Override
    public String getText() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return "Player";
        return mc.player.getName().getString();
    }
}

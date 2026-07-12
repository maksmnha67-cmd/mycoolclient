package com.mycoolclient.hud.modules;

import com.mycoolclient.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;

/**
 * Показывает активные эффекты зелий и оставшееся время.
 * Эта информация и так видна в инвентаре — здесь просто удобнее вынесена на HUD.
 */
public class PotionTimersHud extends HudModule {

    public PotionTimersHud() {
        super("PotionTimers", "Показывает активные эффекты зелий", 10, 130);
        setAccentColor(0xFFB16BFF);
    }

    @Override
    public String getText() {
        return ""; // рисуем построчно в render()
    }

    @Override
    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        float offsetY = 0;
        for (StatusEffectInstance effect : mc.player.getStatusEffects()) {
            String name = effect.getEffectType().getName().getString();
            int seconds = effect.getDuration() / 20;
            String text = String.format("%s %d:%02d", name, seconds / 60, seconds % 60);

            int textWidth = mc.textRenderer.getWidth(text);
            int boxWidth = textWidth + 19;
            int boxHeight = mc.textRenderer.fontHeight + 8;

            net.minecraft.client.gui.DrawableHelper.fill(matrices,
                    (int) x, (int) (y + offsetY),
                    (int) (x + boxWidth), (int) (y + offsetY + boxHeight),
                    0xB0101014);
            net.minecraft.client.gui.DrawableHelper.fill(matrices,
                    (int) x, (int) (y + offsetY),
                    (int) x + 3, (int) (y + offsetY + boxHeight),
                    accentColor);
            mc.textRenderer.draw(matrices, text, x + 11, y + offsetY + 4, 0xFFFFFFFF);

            offsetY += boxHeight + 3;
        }
    }
}

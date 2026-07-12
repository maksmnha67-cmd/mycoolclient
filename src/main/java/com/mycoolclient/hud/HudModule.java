package com.mycoolclient.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mycoolclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Базовый HUD-модуль. Рисует "капсулу" в стиле Rockstar Client:
 * тёмная полупрозрачная плашка + цветной акцент слева + текст.
 * Позиция каждого элемента настраивается через drag&drop в ClickGUI (см. HudEditorScreen).
 */
public abstract class HudModule extends Module {

    protected float x;
    protected float y;
    protected int accentColor = 0xFF00D9FF; // акцентный голубой, можно менять в GUI

    public HudModule(String name, String description, float defaultX, float defaultY) {
        super(name, description, Category.HUD, true);
        this.x = defaultX;
        this.y = defaultY;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) { this.x = x; this.y = y; }

    public void setAccentColor(int color) { this.accentColor = color; }
    public int getAccentColor() { return accentColor; }

    /** Текст, который выводится в капсуле. Реализуется в наследниках. */
    public abstract String getText();

    /** Отрисовка одной "капсулы" HUD в фирменном стиле. */
    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer font = mc.textRenderer;
        String text = getText();

        int textWidth = font.getWidth(text);
        int paddingX = 8;
        int paddingY = 4;
        int accentWidth = 3;

        int boxWidth = textWidth + paddingX * 2 + accentWidth;
        int boxHeight = font.fontHeight + paddingY * 2;

        RenderSystem.enableBlend();

        // фон-плашка (полупрозрачный тёмный)
        fill(matrices, x, y, x + boxWidth, y + boxHeight, 0xB0101014);

        // акцентная полоса слева
        fill(matrices, x, y, x + accentWidth, y + boxHeight, accentColor);

        // текст
        font.draw(matrices, text,
                x + accentWidth + paddingX,
                y + paddingY,
                0xFFFFFFFF);

        RenderSystem.disableBlend();
    }

    private void fill(MatrixStack matrices, float x1, float y1, float x2, float y2, int color) {
        net.minecraft.client.gui.DrawableHelper.fill(matrices,
                (int) x1, (int) y1, (int) x2, (int) y2, color);
    }
}

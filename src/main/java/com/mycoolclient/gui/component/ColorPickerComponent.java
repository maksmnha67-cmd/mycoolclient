package com.mycoolclient.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Consumer;

/**
 * Простой RGB color-picker: три слайдера (R, G, B) + превью-квадрат с итоговым цветом.
 * Используется для настройки цвета визуальных эффектов (трейл, акценты HUD и т.д.).
 */
public class ColorPickerComponent extends DrawableHelper {

    private final SliderComponent rSlider, gSlider, bSlider;
    private int r, g, b;
    private final Consumer<Integer> onChange;
    private int x, y;

    public ColorPickerComponent(int initialColor, Consumer<Integer> onChange) {
        this.onChange = onChange;
        this.r = (initialColor >> 16) & 0xFF;
        this.g = (initialColor >> 8) & 0xFF;
        this.b = initialColor & 0xFF;

        rSlider = new SliderComponent("R", 0, 255, r, v -> { r = v.intValue(); pushColor(); });
        gSlider = new SliderComponent("G", 0, 255, g, v -> { g = v.intValue(); pushColor(); });
        bSlider = new SliderComponent("B", 0, 255, b, v -> { b = v.intValue(); pushColor(); });
    }

    private void pushColor() {
        int color = (0xFF << 24) | (r << 16) | (g << 8) | b;
        onChange.accept(color);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        rSlider.setPosition(x, y);
        gSlider.setPosition(x, y + 22);
        bSlider.setPosition(x, y + 44);
    }

    public int getHeight() {
        return 22 * 3 + 20; // 3 слайдера + превью
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        rSlider.render(matrices, mouseX, mouseY);
        gSlider.render(matrices, mouseX, mouseY);
        bSlider.render(matrices, mouseX, mouseY);

        RenderSystem.enableBlend();
        int previewY = y + 44 + 22;
        int color = (0xFF << 24) | (r << 16) | (g << 8) | b;
        fill(matrices, x, previewY, x + 160, previewY + 16, color);
        RenderSystem.disableBlend();
    }

    public void mouseClicked(double mouseX, double mouseY) {
        rSlider.mouseClicked(mouseX, mouseY);
        gSlider.mouseClicked(mouseX, mouseY);
        bSlider.mouseClicked(mouseX, mouseY);
    }

    public void mouseDragged(double mouseX) {
        rSlider.mouseDragged(mouseX);
        gSlider.mouseDragged(mouseX);
        bSlider.mouseDragged(mouseX);
    }

    public void mouseReleased() {
        rSlider.mouseReleased();
        gSlider.mouseReleased();
        bSlider.mouseReleased();
    }
}

package com.mycoolclient.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Consumer;

/** Слайдер для числовых настроек модулей (например, размер шрифта HUD, толщина акцента и т.п.) */
public class SliderComponent extends DrawableHelper {

    private final String label;
    private final float min, max;
    private float value;
    private final Consumer<Float> onChange;
    private boolean dragging = false;

    private int x, y, width = 160, height = 18;

    public SliderComponent(String label, float min, float max, float initial, Consumer<Float> onChange) {
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = initial;
        this.onChange = onChange;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        fill(matrices, x, y, x + width, y + height, 0x60101014);

        float percent = (value - min) / (max - min);
        int fillWidth = (int) (width * percent);
        fill(matrices, x, y, x + fillWidth, y + height, 0xFF00D9FF);

        var font = MinecraftClient.getInstance().textRenderer;
        String text = label + ": " + String.format("%.1f", value);
        font.draw(matrices, text, x + 4, y + 5, 0xFFFFFFFF);
        RenderSystem.disableBlend();
    }

    public void mouseClicked(double mouseX, double mouseY) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            dragging = true;
            updateValue(mouseX);
        }
    }

    public void mouseReleased() {
        dragging = false;
    }

    public void mouseDragged(double mouseX) {
        if (dragging) updateValue(mouseX);
    }

    private void updateValue(double mouseX) {
        float percent = (float) ((mouseX - x) / width);
        percent = Math.max(0, Math.min(1, percent));
        value = min + percent * (max - min);
        onChange.accept(value);
    }

    public boolean isDragging() {
        return dragging;
    }
}

package com.mycoolclient.hud.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mycoolclient.hud.HudModule;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

/**
 * Классический Keystrokes HUD — показывает нажатые W/A/S/D и клики мыши.
 * Чисто визуальная фича для стрима/видео, распространена во ВСЕХ клиентах (включая ванильные оверлеи).
 */
public class KeystrokesHud extends HudModule {

    public KeystrokesHud() {
        super("Keystrokes", "Показывает нажатия WASD и кнопок мыши", 10, 100);
    }

    @Override
    public String getText() {
        return ""; // используем кастомный render()
    }

    @Override
    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen != null) return;

        int size = 20;
        int gap = 2;
        float baseX = getX();
        float baseY = getY();

        drawKey(matrices, baseX + size + gap, baseY, size, "W", isDown(GLFW.GLFW_KEY_W));
        drawKey(matrices, baseX, baseY + size + gap, size, "A", isDown(GLFW.GLFW_KEY_A));
        drawKey(matrices, baseX + size + gap, baseY + size + gap, size, "S", isDown(GLFW.GLFW_KEY_S));
        drawKey(matrices, baseX + (size + gap) * 2, baseY + size + gap, size, "D", isDown(GLFW.GLFW_KEY_D));

        // мышь
        int mouseY = (int) (baseY + (size + gap) * 2 + 4);
        drawMouseButton(matrices, baseX, mouseY, 18, 26, "L", mc.options.keyAttack.isPressed());
        drawMouseButton(matrices, baseX + 20, mouseY, 18, 26, "R", mc.options.keyUse.isPressed());
    }

    private boolean isDown(int glfwKey) {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return GLFW.glfwGetKey(handle, glfwKey) == GLFW.GLFW_PRESS;
    }

    private void drawKey(MatrixStack matrices, float x, float y, int size, String label, boolean pressed) {
        int bg = pressed ? 0xFF00D9FF : 0xB0101014;
        int textColor = pressed ? 0xFF101014 : 0xFFFFFFFF;
        DrawableHelper.fill(matrices, (int) x, (int) y, (int) (x + size), (int) (y + size), bg);
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        int tw = font.getWidth(label);
        font.draw(matrices, label, x + (size - tw) / 2f, y + (size - font.fontHeight) / 2f, textColor);
    }

    private void drawMouseButton(MatrixStack matrices, float x, float y, int w, int h, String label, boolean pressed) {
        int bg = pressed ? 0xFF00D9FF : 0xB0101014;
        int textColor = pressed ? 0xFF101014 : 0xFFFFFFFF;
        DrawableHelper.fill(matrices, (int) x, (int) y, (int) (x + w), (int) (y + h), bg);
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        int tw = font.getWidth(label);
        font.draw(matrices, label, x + (w - tw) / 2f, y + (h - font.fontHeight) / 2f, textColor);
    }
}

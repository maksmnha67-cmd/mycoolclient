package com.mycoolclient.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mycoolclient.module.Module;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

/** Строка модуля в ClickGUI: имя + переключатель, в стиле Pouch Client (закруглённые тумблеры). */
public class ModuleToggleComponent extends DrawableHelper {

    private final Module module;
    private int x, y, width = 200, height = 22;

    public ModuleToggleComponent(Module module) {
        this.module = module;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        fill(matrices, x, y, x + width, y + height, hovered ? 0x50FFFFFF : 0x30101014);

        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        font.draw(matrices, module.getName(), x + 8, y + (height - font.fontHeight) / 2f, 0xFFFFFFFF);

        // тумблер справа
        int toggleW = 30, toggleH = 14;
        int toggleX = x + width - toggleW - 6;
        int toggleY = y + (height - toggleH) / 2;

        int bg = module.isEnabled() ? 0xFF00D9FF : 0xFF3A3A40;
        fill(matrices, toggleX, toggleY, toggleX + toggleW, toggleY + toggleH, bg);

        int knobSize = toggleH - 4;
        int knobX = module.isEnabled() ? toggleX + toggleW - knobSize - 2 : toggleX + 2;
        fill(matrices, knobX, toggleY + 2, knobX + knobSize, toggleY + 2 + knobSize, 0xFFFFFFFF);

        RenderSystem.disableBlend();
    }

    public void mouseClicked(double mouseX, double mouseY) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            module.toggle();
        }
    }
}

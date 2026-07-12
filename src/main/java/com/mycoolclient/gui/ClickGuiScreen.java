package com.mycoolclient.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mycoolclient.gui.component.ColorPickerComponent;
import com.mycoolclient.gui.component.ModuleToggleComponent;
import com.mycoolclient.gui.component.SliderComponent;
import com.mycoolclient.module.Module;
import com.mycoolclient.module.ModuleManager;
import com.mycoolclient.module.modules.AngelHaloModule;
import com.mycoolclient.module.modules.BlockOutlineModule;
import com.mycoolclient.module.modules.ChinaHatModule;
import com.mycoolclient.module.modules.HandVisualsModule;
import com.mycoolclient.module.modules.TargetHighlightModule;
import com.mycoolclient.module.modules.WingsModule;
import com.mycoolclient.module.modules.TrailModule;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

/**
 * ClickGUI в стиле Pouch Client: тёмная панель, слева вертикальный список категорий,
 * справа — модули выбранной категории с тумблерами.
 */
public class ClickGuiScreen extends Screen {

    private Module.Category selectedCategory = Module.Category.HUD;
    private final List<ModuleToggleComponent> toggles = new ArrayList<>();

    // расширенные контролы для VISUAL-модулей
    private ColorPickerComponent trailColorPicker;
    private SliderComponent trailDensitySlider;
    private SliderComponent handOffsetXSlider, handOffsetYSlider, handOffsetZSlider;
    private ColorPickerComponent ghostColorPicker;
    private SliderComponent ghostDensitySlider;
    private int ghostStyleButtonY = -1; // для клика по кнопке смены стиля
    private ColorPickerComponent hatColorPicker;
    private SliderComponent hatScaleSlider;
    private ColorPickerComponent outlineColorPicker;
    private SliderComponent outlineThicknessSlider;
    private ColorPickerComponent haloColorPicker;
    private SliderComponent haloScaleSlider;
    private ColorPickerComponent wingsColorPicker;
    private SliderComponent wingsScaleSlider;
    private int wingsStyleButtonY = -1;

    private final int panelX = 40;
    private final int panelY = 30;
    private final int panelWidth = 420;
    private final int panelHeight = 300;

    private final int categoryWidth = 120;

    public ClickGuiScreen() {
        super(new LiteralText("MyCoolClient GUI"));
    }

    @Override
    protected void init() {
        rebuildToggles();
    }

    private void rebuildToggles() {
        toggles.clear();
        trailColorPicker = null;
        trailDensitySlider = null;
        handOffsetXSlider = null;
        handOffsetYSlider = null;
        handOffsetZSlider = null;
        ghostColorPicker = null;
        ghostDensitySlider = null;
        ghostStyleButtonY = -1;
        hatColorPicker = null;
        hatScaleSlider = null;
        outlineColorPicker = null;
        outlineThicknessSlider = null;
        haloColorPicker = null;
        haloScaleSlider = null;
        wingsColorPicker = null;
        wingsScaleSlider = null;
        wingsStyleButtonY = -1;

        int startY = panelY + 40;
        int i = 0;
        for (Module module : ModuleManager.getByCategory(selectedCategory)) {
            ModuleToggleComponent toggle = new ModuleToggleComponent(module);
            toggle.setPosition(panelX + categoryWidth + 20, startY + i * 26);
            toggles.add(toggle);
            i++;

            int extraX = panelX + categoryWidth + 40;
            int extraY = startY + i * 26 + 4;

            if (module instanceof TrailModule) {
                TrailModule trail = (TrailModule) module;
                trailColorPicker = new ColorPickerComponent(trail.getColor(), trail::setColor);
                trailColorPicker.setPosition(extraX, extraY);

                trailDensitySlider = new SliderComponent("Density", 1, 10, trail.getDensity(), trail::setDensity);
                trailDensitySlider.setPosition(extraX, extraY + trailColorPicker.getHeight() + 4);

                i += 6; // резервируем место под контролы в списке
            }

            if (module instanceof HandVisualsModule) {
                HandVisualsModule hv = (HandVisualsModule) module;

                handOffsetXSlider = new SliderComponent("Offset X", -1f, 1f, hv.getOffsetX(), hv::setOffsetX);
                handOffsetXSlider.setPosition(extraX, extraY);

                handOffsetYSlider = new SliderComponent("Offset Y", -1f, 1f, hv.getOffsetY(), hv::setOffsetY);
                handOffsetYSlider.setPosition(extraX, extraY + 22);

                handOffsetZSlider = new SliderComponent("Offset Z", -1f, 1f, hv.getOffsetZ(), hv::setOffsetZ);
                handOffsetZSlider.setPosition(extraX, extraY + 44);

                i += 4;
            }

            if (module instanceof TargetHighlightModule) {
                TargetHighlightModule th = (TargetHighlightModule) module;

                ghostStyleButtonY = extraY; // клик по этой строке = смена стиля
                ghostColorPicker = new ColorPickerComponent(th.getColor(), th::setColor);
                ghostColorPicker.setPosition(extraX, extraY + 16);

                ghostDensitySlider = new SliderComponent("Particles", 1, 10, th.getParticleCount(), th::setParticleCount);
                ghostDensitySlider.setPosition(extraX, extraY + 16 + ghostColorPicker.getHeight() + 4);

                i += 7;
            }

            if (module instanceof ChinaHatModule) {
                ChinaHatModule hat = (ChinaHatModule) module;

                hatColorPicker = new ColorPickerComponent(hat.getColor(), hat::setColor);
                hatColorPicker.setPosition(extraX, extraY);

                hatScaleSlider = new SliderComponent("Scale", 0.5f, 2.0f, hat.getScale(), hat::setScale);
                hatScaleSlider.setPosition(extraX, extraY + hatColorPicker.getHeight() + 4);

                i += 6;
            }

            if (module instanceof BlockOutlineModule) {
                BlockOutlineModule outline = (BlockOutlineModule) module;

                outlineColorPicker = new ColorPickerComponent(outline.getColor(), outline::setColor);
                outlineColorPicker.setPosition(extraX, extraY);

                outlineThicknessSlider = new SliderComponent("Thickness", 1f, 5f, outline.getThickness(), outline::setThickness);
                outlineThicknessSlider.setPosition(extraX, extraY + outlineColorPicker.getHeight() + 4);

                i += 6;
            }

            if (module instanceof AngelHaloModule) {
                AngelHaloModule halo = (AngelHaloModule) module;

                haloColorPicker = new ColorPickerComponent(halo.getColor(), halo::setColor);
                haloColorPicker.setPosition(extraX, extraY);

                haloScaleSlider = new SliderComponent("Scale", 0.5f, 2.0f, halo.getScale(), halo::setScale);
                haloScaleSlider.setPosition(extraX, extraY + haloColorPicker.getHeight() + 4);

                i += 6;
            }

            if (module instanceof WingsModule) {
                WingsModule wingsModule = (WingsModule) module;

                wingsStyleButtonY = extraY;
                wingsColorPicker = new ColorPickerComponent(wingsModule.getColor(), wingsModule::setColor);
                wingsColorPicker.setPosition(extraX, extraY + 16);

                wingsScaleSlider = new SliderComponent("Scale", 0.5f, 2.0f, wingsModule.getScale(), wingsModule::setScale);
                wingsScaleSlider.setPosition(extraX, extraY + 16 + wingsColorPicker.getHeight() + 4);

                i += 7;
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        RenderSystem.enableBlend();

        // основная панель
        fill(matrices, panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xE0141418);
        // заголовок
        fill(matrices, panelX, panelY, panelX + panelWidth, panelY + 30, 0xFF1B1B22);
        textRenderer.draw(matrices, "MyCoolClient", panelX + 12, panelY + 10, 0xFF00D9FF);

        // панель категорий
        fill(matrices, panelX, panelY + 30, panelX + categoryWidth, panelY + panelHeight, 0xFF1B1B22);

        int catY = panelY + 40;
        for (Module.Category category : Module.Category.values()) {
            boolean selected = category == selectedCategory;
            if (selected) {
                fill(matrices, panelX, catY - 4, panelX + categoryWidth, catY + 16, 0xFF23232B);
            }
            int color = selected ? 0xFF00D9FF : 0xFFAAAAAA;
            textRenderer.draw(matrices, category.name(), panelX + 12, catY, color);
            catY += 26;
        }

        // список модулей выбранной категории
        for (ModuleToggleComponent toggle : toggles) {
            toggle.render(matrices, mouseX, mouseY);
        }

        // расширенные контролы (только если соответствующий модуль включён)
        if (trailColorPicker != null) {
            trailColorPicker.render(matrices, mouseX, mouseY);
            trailDensitySlider.render(matrices, mouseX, mouseY);
        }
        if (handOffsetXSlider != null) {
            handOffsetXSlider.render(matrices, mouseX, mouseY);
            handOffsetYSlider.render(matrices, mouseX, mouseY);
            handOffsetZSlider.render(matrices, mouseX, mouseY);
        }
        if (ghostColorPicker != null) {
            String styleLabel = "Style: " + getSelectedStyleName() + "  (click to change)";
            textRenderer.draw(matrices, styleLabel, panelX + categoryWidth + 40, ghostStyleButtonY, 0xFF00D9FF);
            ghostColorPicker.render(matrices, mouseX, mouseY);
            ghostDensitySlider.render(matrices, mouseX, mouseY);
        }
        if (hatColorPicker != null) {
            hatColorPicker.render(matrices, mouseX, mouseY);
            hatScaleSlider.render(matrices, mouseX, mouseY);
        }
        if (outlineColorPicker != null) {
            outlineColorPicker.render(matrices, mouseX, mouseY);
            outlineThicknessSlider.render(matrices, mouseX, mouseY);
        }
        if (haloColorPicker != null) {
            haloColorPicker.render(matrices, mouseX, mouseY);
            haloScaleSlider.render(matrices, mouseX, mouseY);
        }
        if (wingsColorPicker != null) {
            WingsModule wm = (WingsModule) ModuleManager.getByName("Wings");
            String styleLabel = "Style: " + (wm != null ? wm.getStyle().name() : "-") + "  (click to change)";
            textRenderer.draw(matrices, styleLabel, panelX + categoryWidth + 40, wingsStyleButtonY, 0xFF00D9FF);
            wingsColorPicker.render(matrices, mouseX, mouseY);
            wingsScaleSlider.render(matrices, mouseX, mouseY);
        }

        RenderSystem.disableBlend();
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // клик по категории
        int catY = panelY + 40;
        for (Module.Category category : Module.Category.values()) {
            if (mouseX >= panelX && mouseX <= panelX + categoryWidth
                    && mouseY >= catY - 4 && mouseY <= catY + 16) {
                selectedCategory = category;
                rebuildToggles();
                return true;
            }
            catY += 26;
        }

        for (ModuleToggleComponent toggle : toggles) {
            toggle.mouseClicked(mouseX, mouseY);
        }

        if (trailColorPicker != null) {
            trailColorPicker.mouseClicked(mouseX, mouseY);
            trailDensitySlider.mouseClicked(mouseX, mouseY);
        }
        if (handOffsetXSlider != null) {
            handOffsetXSlider.mouseClicked(mouseX, mouseY);
            handOffsetYSlider.mouseClicked(mouseX, mouseY);
            handOffsetZSlider.mouseClicked(mouseX, mouseY);
        }
        if (ghostColorPicker != null) {
            // клик по строке "Style: ..." переключает стиль
            if (mouseY >= ghostStyleButtonY - 2 && mouseY <= ghostStyleButtonY + 10
                    && mouseX >= panelX + categoryWidth + 40) {
                for (Module m : ModuleManager.getByCategory(Module.Category.VISUAL)) {
                    if (m instanceof TargetHighlightModule) {
                        ((TargetHighlightModule) m).cycleStyle();
                    }
                }
            }
            ghostColorPicker.mouseClicked(mouseX, mouseY);
            ghostDensitySlider.mouseClicked(mouseX, mouseY);
        }
        if (hatColorPicker != null) {
            hatColorPicker.mouseClicked(mouseX, mouseY);
            hatScaleSlider.mouseClicked(mouseX, mouseY);
        }
        if (outlineColorPicker != null) {
            outlineColorPicker.mouseClicked(mouseX, mouseY);
            outlineThicknessSlider.mouseClicked(mouseX, mouseY);
        }
        if (haloColorPicker != null) {
            haloColorPicker.mouseClicked(mouseX, mouseY);
            haloScaleSlider.mouseClicked(mouseX, mouseY);
        }
        if (wingsColorPicker != null) {
            if (mouseY >= wingsStyleButtonY - 2 && mouseY <= wingsStyleButtonY + 10
                    && mouseX >= panelX + categoryWidth + 40) {
                WingsModule wm = (WingsModule) ModuleManager.getByName("Wings");
                if (wm != null) wm.cycleStyle();
            }
            wingsColorPicker.mouseClicked(mouseX, mouseY);
            wingsScaleSlider.mouseClicked(mouseX, mouseY);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (trailColorPicker != null) {
            trailColorPicker.mouseDragged(mouseX);
            trailDensitySlider.mouseDragged(mouseX);
        }
        if (handOffsetXSlider != null) {
            handOffsetXSlider.mouseDragged(mouseX);
            handOffsetYSlider.mouseDragged(mouseX);
            handOffsetZSlider.mouseDragged(mouseX);
        }
        if (ghostColorPicker != null) {
            ghostColorPicker.mouseDragged(mouseX);
            ghostDensitySlider.mouseDragged(mouseX);
        }
        if (hatColorPicker != null) {
            hatColorPicker.mouseDragged(mouseX);
            hatScaleSlider.mouseDragged(mouseX);
        }
        if (outlineColorPicker != null) {
            outlineColorPicker.mouseDragged(mouseX);
            outlineThicknessSlider.mouseDragged(mouseX);
        }
        if (haloColorPicker != null) {
            haloColorPicker.mouseDragged(mouseX);
            haloScaleSlider.mouseDragged(mouseX);
        }
        if (wingsColorPicker != null) {
            wingsColorPicker.mouseDragged(mouseX);
            wingsScaleSlider.mouseDragged(mouseX);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (trailColorPicker != null) {
            trailColorPicker.mouseReleased();
            trailDensitySlider.mouseReleased();
        }
        if (handOffsetXSlider != null) {
            handOffsetXSlider.mouseReleased();
            handOffsetYSlider.mouseReleased();
            handOffsetZSlider.mouseReleased();
        }
        if (ghostColorPicker != null) {
            ghostColorPicker.mouseReleased();
            ghostDensitySlider.mouseReleased();
        }
        if (hatColorPicker != null) {
            hatColorPicker.mouseReleased();
            hatScaleSlider.mouseReleased();
        }
        if (outlineColorPicker != null) {
            outlineColorPicker.mouseReleased();
            outlineThicknessSlider.mouseReleased();
        }
        if (haloColorPicker != null) {
            haloColorPicker.mouseReleased();
            haloScaleSlider.mouseReleased();
        }
        if (wingsColorPicker != null) {
            wingsColorPicker.mouseReleased();
            wingsScaleSlider.mouseReleased();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // GUI не ставит игру на паузу, как в реальных клиентах
    }

    private String getSelectedStyleName() {
        for (Module m : ModuleManager.getByCategory(Module.Category.VISUAL)) {
            if (m instanceof TargetHighlightModule) {
                return ((TargetHighlightModule) m).getStyle().name();
            }
        }
        return "-";
    }
}

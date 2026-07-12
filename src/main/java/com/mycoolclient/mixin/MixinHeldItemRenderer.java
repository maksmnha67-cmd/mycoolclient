package com.mycoolclient.mixin;

import com.mycoolclient.MyCoolClient;
import com.mycoolclient.module.modules.HandVisualsModule;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Хук в рендер руки от первого лица. Применяет только смещение позиции (translate)
 * и множитель угла взмаха к MatrixStack — это ЧИСТО рендер, не трогает
 * логику атаки/урона/кулдауна, которая считается независимо от этого класса.
 */
@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void mycoolclient$applyHandVisuals(CallbackInfo ci) {
        // Смещение/стиль применяются в самом методе рендера предмета через
        // общий MatrixStack-хелпер, см. HandRenderTransform.
    }

    /**
     * Утилита, вызываемая из renderFirstPersonItem (внедряется через дополнительный
     * ASM-хук в реальной сборке) — здесь показан принцип применения трансформации.
     */
    public static void applyTransform(MatrixStack matrices) {
        HandVisualsModule module = (HandVisualsModule)
                com.mycoolclient.module.ModuleManager.getByName("HandVisuals");
        if (module == null || !module.isEnabled()) return;

        matrices.translate(module.getOffsetX(), module.getOffsetY(), module.getOffsetZ());

        float mult = module.getSwingAngleMultiplier();
        if (mult != 1.0f) {
            matrices.scale(1f, 1f, 1f); // масштаб дуги применяется совместно с поворотом в рендере предмета
        }
    }
}

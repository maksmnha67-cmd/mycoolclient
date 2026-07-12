package com.mycoolclient.mixin;

import com.mycoolclient.module.ModuleManager;
import com.mycoolclient.module.modules.BlockOutlineModule;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Перехватывает вызов drawBlockOutline (стандартный ванильный "блок в прицеле")
 * и подменяет RGBA на цвет из BlockOutlineModule, если модуль включён.
 * Сам факт "какой блок подсвечен и когда" не меняется — это всё ещё ровно
 * тот блок, который ванильная игра и так обвела бы чёрным. Меняется только цвет/толщина.
 */
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @ModifyArgs(method = "drawBlockOutline", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"
    ))
    private void mycoolclient$customOutlineColor(Args args) {
        BlockOutlineModule module = (BlockOutlineModule) ModuleManager.getByName("BlockOutline");
        if (module == null || !module.isEnabled()) return;

        int color = module.getColor();
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        args.set(0, r);
        args.set(1, g);
        args.set(2, b);
        args.set(3, a);
    }
}

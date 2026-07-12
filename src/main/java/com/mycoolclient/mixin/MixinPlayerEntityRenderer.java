package com.mycoolclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mycoolclient.module.ModuleManager;
import com.mycoolclient.module.modules.AngelHaloModule;
import com.mycoolclient.module.modules.ChinaHatModule;
import com.mycoolclient.module.modules.WingsModule;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Рисует China Hat (конусную шляпу) поверх головы игрока — чистая косметика,
 * как кастомная капа. Не даёт игрового преимущества, отрисовывается для
 * ЛЮБОГО игрока (не только для себя), точно так же, как капы видны другим.
 */
@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {

    @Inject(method = "render", at = @At("TAIL"))
    private void mycoolclient$renderChinaHat(AbstractClientPlayerEntity player, float yaw, float tickDelta,
                                              MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                              int light, CallbackInfo ci) {
        ChinaHatModule hat = (ChinaHatModule) ModuleManager.getByName("ChinaHat");
        if (hat == null || !hat.isEnabled()) return;

        matrices.push();
        // поднимаем над головой игрока (модель головы примерно на высоте ~1.5, шляпа чуть выше)
        matrices.translate(0.0, 1.62 * hat.getScale(), 0.0);
        matrices.scale(hat.getScale(), hat.getScale(), hat.getScale());

        int color = hat.getColor();
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        Matrix4f model = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        // вершина конуса
        buffer.vertex(model, 0f, 0.5f, 0f).color(r, g, b, a).next();

        // основание конуса — окружность
        int segments = 12;
        float radius = 0.4f;
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float x = (float) Math.cos(angle) * radius;
            float z = (float) Math.sin(angle) * radius;
            buffer.vertex(model, x, 0f, z).color(r, g, b, a).next();
        }

        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrices.pop();

        mycoolclient$renderHalo(player, matrices, vertexConsumers);
        mycoolclient$renderWings(player, matrices, vertexConsumers);
    }

    private void mycoolclient$renderHalo(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        AngelHaloModule halo = (AngelHaloModule) ModuleManager.getByName("AngelHalo");
        if (halo == null || !halo.isEnabled()) return;

        matrices.push();
        matrices.translate(0.0, 1.9 * halo.getScale(), 0.0);
        matrices.scale(halo.getScale(), halo.getScale(), halo.getScale());

        int color = halo.getColor();
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        Matrix4f model = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        int segments = 16;
        float outerR = 0.35f;
        float innerR = 0.26f;
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            buffer.vertex(model, cos * outerR, 0f, sin * outerR).color(r, g, b, a).next();
            buffer.vertex(model, cos * innerR, 0f, sin * innerR).color(r, g, b, a).next();
        }
        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrices.pop();
    }

    private void mycoolclient$renderWings(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        WingsModule wings = (WingsModule) ModuleManager.getByName("Wings");
        if (wings == null || !wings.isEnabled()) return;

        matrices.push();
        matrices.translate(0.0, 1.1, 0.15); // за спиной
        matrices.scale(wings.getScale(), wings.getScale(), wings.getScale());

        int color = wings.getColor();
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        Matrix4f model = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);

        // левое крыло (треугольник)
        buffer.vertex(model, 0.05f, 0.3f, 0f).color(r, g, b, a).next();
        buffer.vertex(model, -0.6f, 0.4f, 0.1f).color(r, g, b, a).next();
        buffer.vertex(model, -0.5f, -0.3f, 0.1f).color(r, g, b, a).next();

        // правое крыло (треугольник, зеркально)
        buffer.vertex(model, -0.05f, 0.3f, 0f).color(r, g, b, a).next();
        buffer.vertex(model, 0.6f, 0.4f, 0.1f).color(r, g, b, a).next();
        buffer.vertex(model, 0.5f, -0.3f, 0.1f).color(r, g, b, a).next();

        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrices.pop();
    }
}
}

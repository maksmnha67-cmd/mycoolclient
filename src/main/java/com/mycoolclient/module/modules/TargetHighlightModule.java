package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * Партикл-подсветка игрока, на которого наведён прицел ВПЛОТНУЮ.
 *
 * Почему это не ESP (см. предыдущее обсуждение):
 * - используется mc.crosshairTarget — ровно то, во что ты можешь ударить прямо сейчас;
 * - жёсткий потолок дистанции ~3.5 блока, не настраивается через GUI;
 * - блок между тобой и целью обрывает эффект (crosshairTarget не даёт результата сквозь стены);
 * - никакого HP/брони/ника не показывается — только декоративные частицы.
 *
 * Стили (Square/Circle/Ghost/Devil) — это просто разный узор частиц,
 * функционально они одинаковы и одинаково ограничены.
 */
public class TargetHighlightModule extends Module {

    public enum Style {
        SQUARE,
        CIRCLE,
        GHOST,
        DEVIL
    }

    private static final double MAX_INTERACT_DISTANCE = 3.5;

    private Style style = Style.GHOST;
    private int color = 0x7A7CFF88;
    private float particleCount = 4f;

    public TargetHighlightModule() {
        super("TargetHighlight", "Партикл-подсветка цели вплотную (Square/Circle/Ghost/Devil)", Category.VISUAL, false);
    }

    public Style getStyle() { return style; }
    public void setStyle(Style style) { this.style = style; }
    public void cycleStyle() {
        Style[] values = Style.values();
        style = values[(style.ordinal() + 1) % values.length];
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public float getParticleCount() { return particleCount; }
    public void setParticleCount(float v) { particleCount = v; }

    public void tick() {
        if (!isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.crosshairTarget == null) return;
        if (mc.crosshairTarget.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult entityHit = (EntityHitResult) mc.crosshairTarget;
        if (!(entityHit.getEntity() instanceof LivingEntity)) return;
        if (entityHit.getEntity() == mc.player) return;

        double distance = mc.player.distanceTo(entityHit.getEntity());
        if (distance > MAX_INTERACT_DISTANCE) return; // жёсткая страховка, не настраивается

        LivingEntity target = (LivingEntity) entityHit.getEntity();
        ClientWorld world = mc.world;

        switch (style) {
            case SQUARE: renderSquare(world, target); break;
            case CIRCLE: renderCircle(world, target); break;
            case DEVIL: renderDevil(world, target); break;
            case GHOST:
            default: renderGhost(world, target); break;
        }
    }

    private DustParticleEffect particle(int argb, float scale) {
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        return new DustParticleEffect(r, g, b, scale);
    }

    /** Классический "призрак" — случайные точки по всему объёму хитбокса. */
    private void renderGhost(ClientWorld world, LivingEntity target) {
        DustParticleEffect fx = particle(color, 1.3f);
        double radius = target.getWidth() / 2.0 + 0.15;
        for (int i = 0; i < particleCount; i++) {
            double angle = target.getRandom().nextDouble() * Math.PI * 2;
            double px = target.getX() + Math.cos(angle) * radius;
            double pz = target.getZ() + Math.sin(angle) * radius;
            double py = target.getY() + target.getRandom().nextDouble() * target.getHeight();
            world.addParticle(fx, px, py, pz, 0, 0.01, 0);
        }
    }

    /** Квадратная обводка по контуру хитбокса (как рамка). */
    private void renderSquare(ClientWorld world, LivingEntity target) {
        DustParticleEffect fx = particle(color, 1.0f);
        double half = target.getWidth() / 2.0 + 0.1;
        double height = target.getHeight();
        double baseX = target.getX();
        double baseY = target.getY();
        double baseZ = target.getZ();

        double[][] corners = {
                {-half, -half}, {half, -half}, {half, half}, {-half, half}
        };
        for (double[] corner : corners) {
            world.addParticle(fx, baseX + corner[0], baseY, baseZ + corner[1], 0, 0, 0);
            world.addParticle(fx, baseX + corner[0], baseY + height, baseZ + corner[1], 0, 0, 0);
        }
    }

    /** Плоское кольцо у ног — "аура" на земле. */
    private void renderCircle(ClientWorld world, LivingEntity target) {
        DustParticleEffect fx = particle(color, 1.1f);
        double radius = target.getWidth() / 2.0 + 0.3;
        int points = (int) Math.max(6, particleCount * 2);
        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 / points) * i;
            double px = target.getX() + Math.cos(angle) * radius;
            double pz = target.getZ() + Math.sin(angle) * radius;
            world.addParticle(fx, px, target.getY() + 0.05, pz, 0, 0, 0);
        }
    }

    /** "Дьявольский" стиль — красные частицы + пара "рожек" над головой. */
    private void renderDevil(ClientWorld world, LivingEntity target) {
        int devilColor = 0xB0FF2222;
        DustParticleEffect fx = particle(devilColor, 1.2f);

        double headY = target.getY() + target.getHeight() + 0.1;
        world.addParticle(fx, target.getX() - 0.15, headY, target.getZ(), 0, 0.01, 0);
        world.addParticle(fx, target.getX() + 0.15, headY, target.getZ(), 0, 0.01, 0);

        double radius = target.getWidth() / 2.0 + 0.2;
        for (int i = 0; i < particleCount; i++) {
            double angle = target.getRandom().nextDouble() * Math.PI * 2;
            double px = target.getX() + Math.cos(angle) * radius;
            double pz = target.getZ() + Math.sin(angle) * radius;
            double py = target.getY() + target.getRandom().nextDouble() * target.getHeight() * 0.6;
            world.addParticle(fx, px, py, pz, 0, 0.02, 0);
        }
    }
}

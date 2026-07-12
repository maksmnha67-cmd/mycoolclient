package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3f;

/**
 * Цветной след частиц под ногами игрока во время движения.
 * Используется стандартная ванильная частица DustParticleEffect (та же, что у редстоуна),
 * только с настраиваемым цветом — то есть чисто визуальный эффект,
 * доступный любому серверу с включёнными частицами. Никакого игрового преимущества.
 */
public class TrailModule extends Module {

    private int color = 0xFF00D9FF; // цвет по умолчанию, настраивается через ColorPickerComponent в GUI
    private float density = 3f;     // частиц за тик

    public TrailModule() {
        super("Trail", "Цветной след частиц при движении", Category.VISUAL, false);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    /** Вызывается каждый клиентский тик из MyCoolClient, если модуль включён. */
    public void tick() {
        if (!isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (!(mc.player.horizontalSpeed > 0.005)) return; // только когда реально двигается

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        DustParticleEffect particle = new DustParticleEffect(new Vec3f(r, g, b), 1.0f);

        ClientWorld world = mc.world;
        for (int i = 0; i < density; i++) {
            double offsetX = (mc.player.getRandom().nextDouble() - 0.5) * 0.4;
            double offsetZ = (mc.player.getRandom().nextDouble() - 0.5) * 0.4;
            world.addParticle(particle,
                    mc.player.getX() + offsetX,
                    mc.player.getY() + 0.1,
                    mc.player.getZ() + offsetZ,
                    0, 0, 0);
        }
    }
}

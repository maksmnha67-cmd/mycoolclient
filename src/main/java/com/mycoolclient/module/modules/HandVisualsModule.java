package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;

/**
 * Настройки визуального стиля отрисовки руки/предмета от первого лица:
 * стиль взмаха (амплитуда/дуга анимации) и смещение позиции руки (x/y/z).
 *
 * ВАЖНО: этот модуль трогает только МАТРИЦУ РЕНДЕРА (как рука рисуется на экране).
 * Он не влияет на attack cooldown, hit detection, урон или скорость атаки —
 * это всё считается ванильной игровой логикой на сервере и клиенте отдельно от рендера.
 * Поэтому это косметика, а не чит (то же самое, что кастомная 3rd-person анимация
 * в ресурспаках или в модах вроде "Not Enough Animations").
 */
public class HandVisualsModule extends Module {

    public enum SwingStyle {
        VANILLA,   // стандартная ванильная анимация
        WIDE,      // более широкая дуга взмаха
        SLOW_ARC,  // плавный медленный взмах (визуально, тайминг атаки не меняется)
        SNAPPY     // короткий резкий взмах
    }

    private SwingStyle swingStyle = SwingStyle.VANILLA;

    // смещение позиции руки/предмета в GUI-пространстве (визуально, в единицах модели)
    private float offsetX = 0f;
    private float offsetY = 0f;
    private float offsetZ = 0f;

    public HandVisualsModule() {
        super("HandVisuals", "Стиль анимации взмаха и позиция руки", Category.VISUAL, false);
    }

    public SwingStyle getSwingStyle() {
        return swingStyle;
    }

    public void setSwingStyle(SwingStyle style) {
        this.swingStyle = style;
    }

    public void cycleSwingStyle() {
        SwingStyle[] values = SwingStyle.values();
        swingStyle = values[(swingStyle.ordinal() + 1) % values.length];
    }

    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }
    public float getOffsetZ() { return offsetZ; }

    public void setOffsetX(float v) { offsetX = v; }
    public void setOffsetY(float v) { offsetY = v; }
    public void setOffsetZ(float v) { offsetZ = v; }

    /**
     * Множитель дуги взмаха для текущего стиля — применяется в MixinHeldItemRenderer
     * только к углу поворота модели, не к игровому таймеру атаки.
     */
    public float getSwingAngleMultiplier() {
        switch (swingStyle) {
            case WIDE: return 1.6f;
            case SLOW_ARC: return 0.7f;
            case SNAPPY: return 1.2f;
            default: return 1.0f;
        }
    }
}

package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;

/**
 * China Hat — классическая косметическая "конусная шляпа" поверх головы игрока,
 * популярная во многих клиентах как чистый визуал (аналог капы). Не даёт
 * никакого игрового преимущества — просто рисуется поверх модели головы.
 * Цвет настраивается через GUI.
 */
public class ChinaHatModule extends Module {

    private int color = 0xFFE0B84B; // соломенно-жёлтый по умолчанию
    private float scale = 1.0f;

    public ChinaHatModule() {
        super("ChinaHat", "Косметическая шляпа на голове (только визуал)", Category.VISUAL, false);
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }
}

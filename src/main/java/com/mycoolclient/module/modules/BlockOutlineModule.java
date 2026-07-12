package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;

/**
 * Красивая цветная обводка блока, на который ты смотришь — по сути то же самое,
 * что ванильный чёрный контур (тоже показывается только для блока в зоне
 * досягаемости, тоже не работает "сквозь" визуальные препятствия), просто
 * с настраиваемым цветом и мягким свечением вместо стандартного чёрного.
 * Не даёт никакой информации, которой нет в ванильной игре.
 */
public class BlockOutlineModule extends Module {

    private int color = 0xFF00D9FF;
    private float thickness = 2.0f;

    public BlockOutlineModule() {
        super("BlockOutline", "Цветная обводка блока в прицеле", Category.VISUAL, false);
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public float getThickness() { return thickness; }
    public void setThickness(float thickness) { this.thickness = thickness; }
}

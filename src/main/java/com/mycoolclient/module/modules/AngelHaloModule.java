package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;

/** Светящийся нимб-кольцо над головой — чистая косметика, видна другим игрокам как капа. */
public class AngelHaloModule extends Module {

    private int color = 0xFFFFF3B0; // тёплый белый/золотой
    private float scale = 1.0f;

    public AngelHaloModule() {
        super("AngelHalo", "Светящийся нимб над головой", Category.VISUAL, false);
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }
}

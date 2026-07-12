package com.mycoolclient.module.modules;

import com.mycoolclient.module.Module;

/** Косметические крылья за спиной — как капа/плащ, чисто визуал. */
public class WingsModule extends Module {

    public enum WingStyle {
        ANGEL,  // белые/золотые перьевые
        DEMON,  // красные/чёрные острые
        FAIRY   // полупрозрачные, "стеклянные"
    }

    private int color = 0xFFFFFFFF;
    private WingStyle style = WingStyle.ANGEL;
    private float scale = 1.0f;

    public WingsModule() {
        super("Wings", "Косметические крылья за спиной", Category.VISUAL, false);
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public WingStyle getStyle() { return style; }
    public void setStyle(WingStyle style) { this.style = style; }
    public void cycleStyle() {
        WingStyle[] values = WingStyle.values();
        style = values[(style.ordinal() + 1) % values.length];
    }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }
}

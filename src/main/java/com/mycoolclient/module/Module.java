package com.mycoolclient.module;

/**
 * Базовый класс для любого модуля (HUD-элемент или фича GUI).
 * Никакой логики, дающей игровое преимущество, здесь нет —
 * только состояние вкл/выкл, категория и биндинг клавиши.
 */
public abstract class Module {

    public enum Category {
        HUD,
        VISUAL,   // косметика: цвета, шейдеры блоков, анимации UI
        MISC      // утилиты: авто-текст, автозамена ников в чате и т.п.
    }

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int keyBind = -1; // GLFW key code, -1 = не привязано

    public Module(String name, String description, Category category, boolean enabledByDefault) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = enabledByDefault;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    /** Вызывается при включении модуля. */
    protected void onEnable() {}

    /** Вызывается при выключении модуля. */
    protected void onDisable() {}
}

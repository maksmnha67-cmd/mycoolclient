package com.mycoolclient.hud.modules;

import com.mycoolclient.hud.HudModule;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Показывает реальное время (часы:минуты), как в Rockstar Client. */
public class ClockHud extends HudModule {

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public ClockHud() {
        super("Clock", "Показывает реальное время", 10, 58);
        setAccentColor(0xFFFFC94B); // жёлто-оранжевый акцент
    }

    @Override
    public String getText() {
        return format.format(new Date());
    }
}

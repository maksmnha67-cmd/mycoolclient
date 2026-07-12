package com.mycoolclient.hud;

import com.mycoolclient.module.Module;
import com.mycoolclient.module.ModuleManager;
import net.minecraft.client.util.math.MatrixStack;

public class HudRenderer {

    public void render(MatrixStack matrices) {
        for (Module module : ModuleManager.getByCategory(Module.Category.HUD)) {
            if (module instanceof HudModule) {
                ((HudModule) module).render(matrices);
            }
        }
    }
}

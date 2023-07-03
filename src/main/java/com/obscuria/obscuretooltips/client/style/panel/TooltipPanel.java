package com.obscuria.obscuretooltips.client.style.panel;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface TooltipPanel {
    void render(GuiGraphics context, Vec2 pos, Point size, float seconds, boolean slot);
    default void reset() {}
}

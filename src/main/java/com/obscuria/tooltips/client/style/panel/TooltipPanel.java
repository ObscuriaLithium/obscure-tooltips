package com.obscuria.tooltips.client.style.panel;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface TooltipPanel {
    void render(TooltipRenderer renderer, Vec2 pos, Point size, boolean slot);
    default void reset() {}
}

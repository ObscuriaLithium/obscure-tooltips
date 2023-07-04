package com.obscuria.tooltips.client.style.icon;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface TooltipIcon {
    void render(TooltipRenderer renderer, int x, int y);
    default void reset() {}
}

package com.obscuria.tooltips.client.style.effect;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import com.obscuria.tooltips.client.style.Effects;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface TooltipEffect {
    void render(TooltipRenderer renderer, Vec2 pos, Point size);
    default void reset() {}
    default Effects.Order order() {
        return Effects.Order.LAYER_3_TEXT$FRAME;
    }

    default Effects.Category category() {
        return Effects.Category.NONE;
    }
}

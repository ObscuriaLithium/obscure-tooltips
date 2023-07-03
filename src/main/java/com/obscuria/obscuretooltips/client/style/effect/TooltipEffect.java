package com.obscuria.obscuretooltips.client.style.effect;

import com.obscuria.obscuretooltips.client.style.Effects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface TooltipEffect {
    void render(GuiGraphics context, Vec2 pos, Point size, float seconds);
    default void reset() {}
    default Effects.Order order() {
        return Effects.Order.LAYER_3_TEXT$FRAME;
    }

    @SuppressWarnings("unused")
    default Effects.Category category() {
        return Effects.Category.ENCHANTMENT;
    }
}

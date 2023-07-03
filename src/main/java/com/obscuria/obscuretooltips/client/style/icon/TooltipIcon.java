package com.obscuria.obscuretooltips.client.style.icon;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface TooltipIcon {
    void render(GuiGraphics context, ItemStack stack, int x, int y, float seconds);
    default void reset() {}
}

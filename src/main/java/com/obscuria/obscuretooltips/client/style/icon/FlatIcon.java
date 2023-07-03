package com.obscuria.obscuretooltips.client.style.icon;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class FlatIcon implements TooltipIcon {

    @Override
    public void render(GuiGraphics context, ItemStack stack, int x, int y, float seconds) {
        float scale = seconds < 0.25
                ? (1f - (float) Math.pow(1f - seconds * 4, 3)) * 1.5f
                : seconds < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (seconds - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        context.pose().scale(scale, scale, scale);
        context.renderItem(stack, x, y);
    }
}

package com.obscuria.obscuretooltips.client.style.icon;

import com.obscuria.obscuretooltips.client.renderer.TooltipUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class AnimatedIcon implements TooltipIcon {

    @Override
    public void render(GuiGraphics context, ItemStack stack, int x, int y, float seconds) {
        float scale = seconds < 0.25
                ? (1f - (float) Math.pow(1f - seconds * 4, 3)) * 1.5f
                : seconds < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (seconds - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        context.pose().scale(scale, scale, scale);
        TooltipUtils.renderItem(context, stack, new Vector3f(0, 180f + 180f
                * (1f - (float) Math.pow(1f - Math.min(1f, seconds * 2f), 3f)),
                0), new Vector3f(1));
        //context.renderItem(stack, x, y);
    }
}

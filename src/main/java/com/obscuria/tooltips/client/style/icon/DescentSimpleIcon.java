package com.obscuria.tooltips.client.style.icon;

import com.obscuria.tooltips.client.renderer.TooltipContext;

public class DescentSimpleIcon implements TooltipIcon {

    @Override
    public void render(TooltipContext context, int x, int y) {
        float scale = context.time() < 0.25
                ? (1f - (float) Math.pow(1f - context.time() * 4, 3)) * 1.5f
                : context.time() < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (context.time() - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        context.scale(scale, scale, scale);
        context.context().renderItem(context.stack(), x, y);
    }
}

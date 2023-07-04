package com.obscuria.tooltips.client.style.icon;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;

public class DescentSimpleIcon implements TooltipIcon {

    @Override
    public void render(TooltipRenderer renderer, int x, int y) {
        float scale = renderer.time() < 0.25
                ? (1f - (float) Math.pow(1f - renderer.time() * 4, 3)) * 1.5f
                : renderer.time() < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (renderer.time() - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        renderer.scale(scale, scale, scale);
        renderer.context().renderItem(renderer.stack(), x, y);
    }
}

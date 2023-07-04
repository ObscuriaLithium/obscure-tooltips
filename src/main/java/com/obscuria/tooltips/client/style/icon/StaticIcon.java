package com.obscuria.tooltips.client.style.icon;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;

public class StaticIcon implements TooltipIcon {

    @Override
    public void render(TooltipRenderer renderer, int x, int y) {
        renderer.context().renderItem(renderer.stack(), x, y);
    }
}

package com.obscuria.tooltips.client.style.icon;

import com.obscuria.tooltips.client.renderer.TooltipContext;

public class StaticIcon implements TooltipIcon {

    @Override
    public void render(TooltipContext context, int x, int y) {
        context.context().renderItem(context.stack(), x, y);
    }
}

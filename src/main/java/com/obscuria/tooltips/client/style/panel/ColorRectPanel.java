package com.obscuria.tooltips.client.style.panel;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.world.phys.Vec2;

import java.awt.*;

public class ColorRectPanel implements TooltipPanel {

    private final int BACK_TOP;
    private final int BACK_BOTTOM;
    private final int BORDER_TOP;
    private final int BORDER_BOTTOM;
    private final int SLOT;

    public ColorRectPanel(int backTop, int backBottom, int borderTop, int borderBottom, int slot) {
        BACK_TOP = backTop;
        BACK_BOTTOM = backBottom;
        BORDER_TOP = borderTop;
        BORDER_BOTTOM = borderBottom;
        SLOT = slot;
    }

    @Override
    public void render(TooltipRenderer renderer, Vec2 pos, Point size, boolean slot) {
        final int x = (int) pos.x, y = (int) pos.y;
        TooltipRenderUtil.renderTooltipBackground(renderer.context(), x, y, size.x, size.y, 400, BACK_TOP, BACK_BOTTOM, BORDER_TOP, BORDER_BOTTOM);
        if (!slot) return;
        renderer.push(() -> {
            renderer.translate(0, 0, 400);
            renderer.fillGradient(x + 2, y + 1, 20, 1, SLOT, SLOT);
            renderer.fillGradient(x + 1, y + 2, 22, 20, SLOT, SLOT);
            renderer.fillGradient(x + 2, y + 22, 20, 1, SLOT, SLOT);
        });
    }
}

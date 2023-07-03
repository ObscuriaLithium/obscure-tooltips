package com.obscuria.obscuretooltips.client.style.panel;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(GuiGraphics context, Vec2 pos, Point size, float seconds, boolean slot) {
        final int x = (int) pos.x, y = (int) pos.y;
        TooltipRenderUtil.renderTooltipBackground(context, x, y, size.x, size.y, 400, BACK_TOP, BACK_BOTTOM, BORDER_TOP, BORDER_BOTTOM);
        if (!slot) return;
        context.pose().pushPose();
        context.pose().translate(0, 0, 400);
        context.fillGradient(x + 2, y + 1, x + 22, y + 2, SLOT, SLOT);
        context.fillGradient(x + 1, y + 2, x + 23, y + 22, SLOT, SLOT);
        context.fillGradient(x + 2, y + 22, x + 22, y + 23, SLOT, SLOT);
        context.pose().popPose();
    }

    public static ColorRectPanel build(JsonObject params) {
        return new ColorRectPanel(
                (int) Long.parseLong(params.get("back_top").getAsString(), 16),
                (int) Long.parseLong(params.get("back_bottom").getAsString(), 16),
                (int) Long.parseLong(params.get("border_top").getAsString(), 16),
                (int) Long.parseLong(params.get("border_bottom").getAsString(), 16),
                (int) Long.parseLong(params.get("slot").getAsString(), 16));
    }
}

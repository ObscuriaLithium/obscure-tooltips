package dev.obscuria.tooltips.client.component;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

@Desugar
public record ToolPreviewComponent(ItemStack stack) implements TooltipComponent {

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public int getWidth(FontRenderer font) {
        return ClientConfig.TOOL_PREVIEW_WIDTH.get();
    }

    @Override
    public void renderImage(FontRenderer font, int x, int y, GuiGraphics graphics) {
        final long widthOffset = Math.round(getWidth(font) * 0.46);
        final float spin = (float) (System.currentTimeMillis() / 1000.0 % 360.0) * -20F;

        RenderHelper.enableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + widthOffset, y + 30, 500F);
        GlStateManager.scale(2.75F, 2.75F, 2.75F);
        GlStateManager.rotate(-30, 1.0F, 0.0F, 0.0F); // Axis.XP
        GlStateManager.rotate(spin, 0.0F, 1.0F, 0.0F);      // Axis.YP
        GlStateManager.rotate(-45, 0.0F, 0.0F, 1.0F); // Axis.ZP
        GlStateManager.scale(16.0F, -16.0F, 16.0F);
        graphics.minecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }
}

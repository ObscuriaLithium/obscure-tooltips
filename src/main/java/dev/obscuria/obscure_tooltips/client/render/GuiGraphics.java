package dev.obscuria.obscure_tooltips.client.render;

import dev.obscuria.obscure_tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.obscure_tooltips.util.color.ARGB;
import dev.obscuria.obscure_tooltips.util.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class GuiGraphics {
    private final Pose pose = new Pose();
    private final Minecraft minecraft;

    public GuiGraphics(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Pose pose() {
        return pose;
    }

    public Minecraft minecraft() {
        return minecraft;
    }

    public int guiWidth() {
        return new ScaledResolution(minecraft).getScaledWidth();
    }

    public void fill(int x1, int y1, int x2, int y2, int argb) {
        final ARGB color = Colors.argbOf(argb);
        GraphicUtils.drawRect(this, x1, y1, x2 - x1, y2 - y1, color, color, color, color);
    }

    public void hLine(int minX, int maxX, int y, int argb) {
        if (maxX < minX) {
            final int swap = minX;
            minX = maxX;
            maxX = swap;
        }
        fill(minX, y, maxX + 1, y + 1, argb);
    }

    public void vLine(int x, int minY, int maxY, int argb) {
        if (maxY < minY) {
            final int swap = minY;
            minY = maxY;
            maxY = swap;
        }
        fill(x, minY, x + 1, maxY + 1, argb);
    }

    public void blit(ResourceLocation texture, int x, int y, float u, float v, int width, int height, int texWidth, int texHeight) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(texture);
        Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, texWidth, texHeight);
    }

    public void drawString(FontRenderer font, String text, int x, int y, int argb) {
        font.drawStringWithShadow(text, x, y, argb);
    }

    public void renderItem(ItemStack stack, int x, int y) {
        final IBakedModel model = minecraft.getRenderItem()
                .getItemModelWithOverrides(stack, minecraft.world, minecraft.player);
        final boolean gui3d = model.isGui3d();

        if (gui3d) {
            RenderHelper.enableGUIStandardItemLighting();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 8.0F, y + 8.0F, 150.0F);
        GlStateManager.scale(16.0F, -16.0F, 16.0F);
        minecraft.getRenderItem().renderItem(stack,
                gui3d ? ItemCameraTransforms.TransformType.GUI : ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();
    }

    public void blitGlow(ResourceLocation texture, int x, int y, float u, float v, int width, int height, int texWidth, int texHeight, ARGB argb) {
        GlStateManager.color(argb.red(), argb.green(), argb.blue(), argb.alpha());
        minecraft.getTextureManager().bindTexture(texture);
        GlStateManager.disableAlpha();
        Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, texWidth, texHeight);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void enableScissor(int x1, int y1, int x2, int y2) {
        final ScaledResolution resolution = new ScaledResolution(minecraft);
        final int scale = resolution.getScaleFactor();
        final int framebufferHeight = minecraft.displayHeight;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x1 * scale, framebufferHeight - y2 * scale,
                Math.max(0, (x2 - x1) * scale), Math.max(0, (y2 - y1) * scale));
    }

    public void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}

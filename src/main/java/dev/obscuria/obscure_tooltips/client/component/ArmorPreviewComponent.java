package dev.obscuria.obscure_tooltips.client.component;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;


@Desugar
public record ArmorPreviewComponent(EntityLivingBase armorStand) implements TooltipComponent {

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public int getWidth(FontRenderer font) {
        return ClientConfig.ARMOR_PREVIEW_WIDTH.get();
    }

    @Override
    public void renderImage(FontRenderer font, int x, int y, GuiGraphics graphics) {
        final long widthOffset = Math.round(getWidth(font) * 0.46);
        final float spin = (float) (System.currentTimeMillis() / 1000.0 % 360.0) * 20F;

        RenderHelper.enableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + widthOffset - 2L, y + 57, 500F);
        GlStateManager.scale(-30, -30, 30);
        GlStateManager.rotate(25, 1.0F, 0.0F, 0.0F); // Axis.XP
        GlStateManager.rotate(spin, 0.0F, 1.0F, 0.0F);     // Axis.YP
        final RenderManager dispatcher = Minecraft.getMinecraft().getRenderManager();
        dispatcher.setPlayerViewY(180);
        dispatcher.setRenderShadow(false);
        dispatcher.renderEntity(armorStand, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        dispatcher.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }
}

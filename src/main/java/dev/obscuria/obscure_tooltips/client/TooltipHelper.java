package dev.obscuria.obscure_tooltips.client;

import dev.obscuria.obscure_tooltips.client.component.TextComponent;
import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.config.ClientConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public final class TooltipHelper {
    public static void enableGlowingRenderer() {
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
    }

    public static void disableGlowingRenderer() {
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
    }

    public static int widthOf(List<TooltipComponent> components, FontRenderer font) {
        var max = 0;
        for (var component : components) {
            final var width = component.getWidth(font);
            if (width <= max) continue;
            max = width;
        }
        return max;
    }

    public static int heightOf(List<TooltipComponent> components) {
        var sum = 0;
        for (var component : components) {
            sum += component.getHeight();
        }
        return sum;
    }

//    public static List<TooltipComponent> wrapLines(GuiGraphics graphics, List<TooltipComponent> components, FontRenderer font) {
//        final int maxWidth = (int)(graphics.guiWidth() * 0.5);
//        if (!shouldWrap(components, font, maxWidth)) {
//            return components;
//        }
//        final ArrayList<TooltipComponent> result = new ArrayList<>(components.size() * 2);
//        for (final TooltipComponent component : components) {
//            if (component instanceof final TextComponent tooltip) {
//                if (tooltip.getWidth(font) <= maxWidth) {
//                    result.add(tooltip);
//                }
//                else {
//                    for (String line : font.listFormattedStringToWidth(tooltip.text(), maxWidth)) {
//                        result.add(TooltipComponent.create(line));
//                    }
//                }
//            }
//            else {
//                result.add(component);
//            }
//        }
//        return result;
//    }

    public static List<TooltipComponent> wrapLines(GuiGraphics graphics, List<TooltipComponent> components, FontRenderer font) {
        final var maxWidth = (int) (graphics.guiWidth() * 0.5);
        if (!shouldWrap(components, font, maxWidth)) return components;
        final var result = new ArrayList<TooltipComponent>(components.size() * 2);
        for (var component : components) {
            if (!(component instanceof TextComponent tooltip)) {
                result.add(component);
            } else if (tooltip.getWidth(font) <= maxWidth) {
                result.add(tooltip);
            } else for (String line : font.listFormattedStringToWidth(tooltip.text(), maxWidth)) {
                result.add(TooltipComponent.create(line));
            }
        }
        return result;
    }
//
//    private static boolean shouldWrap(List<TooltipComponent> components, FontRenderer font, int maxWidth) {
//        if (!ClientConfig.AUTO_WRAP_ENABLED.get()) {
//            return false;
//        }
//        for (TooltipComponent component : components) {
//            if (component instanceof TextComponent && component.getWidth(font) > maxWidth) {
//                return true;
//            }
//        }
//        return false;
//    }

    private static boolean shouldWrap(List<TooltipComponent> components, FontRenderer font, int maxWidth) {
        if (!ClientConfig.AUTO_WRAP_ENABLED.get()) return false;
        for (var component : components) {
            if (!(component instanceof TextComponent tooltip)) continue;
            if (tooltip.getWidth(font) <= maxWidth) continue;
            return true;
        }
        return false;
    }
}

package dev.obscuria.tooltips.client.toast;

import dev.obscuria.tooltips.util.color.ARGB;
import dev.obscuria.tooltips.util.color.Colors;
import dev.obscuria.tooltips.util.easing.Easing;
import dev.obscuria.tooltips.util.easing.EasingFunction;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public final class VibrantTooltipsHint implements IToast {
    private static final EasingFunction EASING_1 = Easing.CEIL.mergeOut(Easing.EASE_IN_CUBIC, 0.75f);
    private static final EasingFunction EASING_2 = Easing.EASE_OUT_CUBIC.mergeOut(Easing.FLOOR, 0.25f);
    private static final ARGB TRANSPARENT = Colors.argbOf(0.05f, 1f, 1f, 1f);
    private static final ARGB WHITE = Colors.argbOf(1f, 1f, 1f, 1f);

    private final String label1;
    private final String label2;

    public VibrantTooltipsHint() {
        final String name = TextFormatting.GOLD + "Vibrant Tooltips" + TextFormatting.WHITE;
        this.label1 = I18n.format("toast.obscure_tooltips.hint_1", name);
        this.label2 = I18n.format("toast.obscure_tooltips.hint_2");
    }

    @Override
    @MethodsReturnNonnullByDefault
    public IToast.Visibility draw(GuiToast toastGui, long delta) {
        final Minecraft mc = toastGui.getMinecraft();

        mc.getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 64, 160, 32);

        final var progress = delta / 8000f;

        final int lineCount = mc.fontRenderer.listFormattedStringToWidth(progress < 0.5f ? label1 : label2, 140).size();
        final var y = 16 - lineCount * 4;

        GlStateManager.enableBlend();

        if (progress < 0.5f) {
            final var color = TRANSPARENT.lerp(WHITE, EASING_1.compute(progress / 0.5f));
            GL14.glBlendColor(1f, 1f, 1f, color.alpha());
            GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
            mc.fontRenderer.drawSplitString(label1, 17, y, 140, -1);
        } else {
            final var color = TRANSPARENT.lerp(WHITE, EASING_2.compute((progress - 0.5f) / 0.5f));
            GL14.glBlendColor(1f, 1f, 1f, color.alpha());
            GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
            mc.fontRenderer.drawSplitString(label2, 17, y, 140, -1);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL14.glBlendColor(1f, 1f, 1f, 1f);
        GlStateManager.disableBlend();

        return delta < 8000L ? Visibility.SHOW : Visibility.HIDE;
    }
}

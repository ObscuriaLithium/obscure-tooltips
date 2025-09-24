package dev.obscuria.tooltips.client.element.icon;

import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.element.Transform;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TooltipIcon {

    Codec<TooltipIcon> DIRECT_CODEC = TooltipsRegistries.TOOLTIP_ICON_TYPE.byNameCodec().dispatch(TooltipIcon::codec, Function.identity());
    Codec<TooltipIcon> CODEC = TooltipsRegistries.Resource.TOOLTIP_ICON.byNameCodec();

    Codec<? extends TooltipIcon> codec();

    void render(GuiGraphics graphics, TooltipContext context, int x, int y);

    default void pushTransform(Transform transform, GuiGraphics graphics, TooltipContext context, int x, int y) {
        graphics.pose().pushPose();
        graphics.pose().translate((float) x, (float) y, 150f);
        graphics.pose().translate(transform.offset().x, transform.offset().y, transform.offset().z);
        graphics.pose().scale(transform.scale(), transform.scale(), transform.scale());
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(transform.rotation()));
        applyScale(graphics, context, x, y);
        applyRotation(graphics, context, x, y);

        graphics.pose().pushPose();
        graphics.pose().translate(-8f, -8f, -150f);
    }

    default void popTransform(GuiGraphics graphics) {
        graphics.pose().popPose();
        graphics.pose().popPose();
    }

    default void applyScale(GuiGraphics graphics, TooltipContext context, int x, int y) {}

    default void applyRotation(GuiGraphics graphics, TooltipContext context, int x, int y) {}

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends TooltipIcon>>> registrar) {
        registrar.accept("static", () -> StaticIcon.CODEC);
        registrar.accept("accent", () -> AccentIcon.CODEC);
        registrar.accept("accent_spin", () -> AccentSpinIcon.CODEC);
        registrar.accept("accent_burst", () -> AccentBurstIcon.CODEC);
    }
}

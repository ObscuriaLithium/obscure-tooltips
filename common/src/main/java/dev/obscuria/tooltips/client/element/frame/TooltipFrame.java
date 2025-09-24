package dev.obscuria.tooltips.client.element.frame;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TooltipFrame {

    Codec<TooltipFrame> DIRECT_CODEC = TooltipsRegistries.TOOLTIP_FRAME_TYPE.byNameCodec().dispatch(TooltipFrame::codec, Function.identity());
    Codec<TooltipFrame> CODEC = TooltipsRegistries.Resource.TOOLTIP_FRAME.byNameCodec();

    Codec<? extends TooltipFrame> codec();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends TooltipFrame>>> registrar) {
        registrar.accept("blank", () -> BlankFrame.CODEC);
        registrar.accept("nine_sliced", () -> NineSlicedFrame.CODEC);
    }
}

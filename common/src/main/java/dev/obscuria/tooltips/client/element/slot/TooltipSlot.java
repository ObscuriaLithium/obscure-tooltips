package dev.obscuria.tooltips.client.element.slot;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TooltipSlot {

    Codec<TooltipSlot> DIRECT_CODEC = TooltipsRegistries.TOOLTIP_SLOT_TYPE.byNameCodec().dispatch(TooltipSlot::codec, Function.identity());
    Codec<TooltipSlot> CODEC = TooltipsRegistries.Resource.TOOLTIP_SLOT.byNameCodec();

    Codec<? extends TooltipSlot> codec();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends TooltipSlot>>> registrar) {

        registrar.accept("blank", () -> BlankSlot.CODEC);
        registrar.accept("color_rect", () -> ColorRectSlot.CODEC);
    }
}

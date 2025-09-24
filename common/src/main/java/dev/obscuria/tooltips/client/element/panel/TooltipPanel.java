package dev.obscuria.tooltips.client.element.panel;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TooltipPanel {

    Codec<TooltipPanel> DIRECT_CODEC = TooltipsRegistries.TOOLTIP_PANEL_TYPE.byNameCodec().dispatch(TooltipPanel::codec, Function.identity());
    Codec<TooltipPanel> CODEC = TooltipsRegistries.Resource.TOOLTIP_PANEL.byNameCodec();

    Codec<? extends TooltipPanel> codec();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends TooltipPanel>>> registrar) {

        registrar.accept("blank", () -> BlankPanel.CODEC);
        registrar.accept("color_rect", () -> ColorRectPanel.CODEC);
    }
}

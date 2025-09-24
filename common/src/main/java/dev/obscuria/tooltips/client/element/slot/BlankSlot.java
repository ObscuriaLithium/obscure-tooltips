package dev.obscuria.tooltips.client.element.slot;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;

public record BlankSlot() implements TooltipSlot {

    public static final BlankSlot INSTANCE = new BlankSlot();
    public static final Codec<BlankSlot> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<BlankSlot> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}

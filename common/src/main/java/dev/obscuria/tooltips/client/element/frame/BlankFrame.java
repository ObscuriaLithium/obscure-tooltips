package dev.obscuria.tooltips.client.element.frame;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;

public record BlankFrame() implements TooltipFrame {

    public static final BlankFrame INSTANCE = new BlankFrame();
    public static final Codec<BlankFrame> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<BlankFrame> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}

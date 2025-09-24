package dev.obscuria.tooltips.client.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;

public record QuadPalette(
        ARGB topLeft,
        ARGB topRight,
        ARGB bottomLeft,
        ARGB bottomRight) {

    public static final Codec<QuadPalette> CODEC = RecordCodecBuilder.create(codec -> codec.group(
            ARGB.CODEC.fieldOf("top_left").forGetter(QuadPalette::topLeft),
            ARGB.CODEC.fieldOf("top_right").forGetter(QuadPalette::topRight),
            ARGB.CODEC.fieldOf("bottom_left").forGetter(QuadPalette::bottomLeft),
            ARGB.CODEC.fieldOf("bottom_right").forGetter(QuadPalette::bottomRight)
    ).apply(codec, QuadPalette::new));
}

package dev.obscuria.tooltips.client.style

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.ARGB

class QuadPalette(
    val topLeft: ARGB,
    val topRight: ARGB,
    val bottomLeft: ARGB,
    val bottomRight: ARGB
) {

    companion object {

        val CODEC: Codec<QuadPalette> = RecordCodecBuilder.create { codec ->
            codec.group(
                ARGB.CODEC
                    .fieldOf("top_left")
                    .forGetter(QuadPalette::topLeft),
                ARGB.CODEC
                    .fieldOf("top_right")
                    .forGetter(QuadPalette::topRight),
                ARGB.CODEC
                    .fieldOf("bottom_left")
                    .forGetter(QuadPalette::bottomLeft),
                ARGB.CODEC
                    .fieldOf("bottom_right")
                    .forGetter(QuadPalette::bottomRight)
            ).apply(codec, ::QuadPalette)
        }
    }
}
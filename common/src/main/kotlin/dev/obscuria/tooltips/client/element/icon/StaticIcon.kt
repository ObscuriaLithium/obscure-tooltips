package dev.obscuria.tooltips.client.element.icon

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.phys.Vec3
import java.util.*

class StaticIcon(
    offset: Optional<Vec3>,
    scale: Optional<Float>
) : TransformableIcon(offset, scale) {

    override fun codec(): Codec<out ITooltipIcon> = CODEC

    companion object {

        val CODEC: Codec<StaticIcon> = RecordCodecBuilder.create { codec ->
            codec.group(
                Vec3.CODEC
                    .optionalFieldOf("offset")
                    .forGetter(StaticIcon::offset),
                Codec.FLOAT
                    .optionalFieldOf("scale")
                    .forGetter(StaticIcon::scale)
            ).apply(codec, ::StaticIcon)
        }
    }
}
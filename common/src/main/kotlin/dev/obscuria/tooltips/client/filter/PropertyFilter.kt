package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.jvm.optionals.getOrNull

class PropertyFilter(
    val hasFoil: Optional<Boolean>
): IItemFilter {

    override fun codec(): Codec<PropertyFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return hasFoil.getOrNull()?.equals(stack.hasFoil()) == true
    }

    companion object {

        val CODEC: Codec<PropertyFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.BOOL
                    .optionalFieldOf("has_foil")
                    .forGetter(PropertyFilter::hasFoil)
            ).apply(codec, ::PropertyFilter)
        }
    }
}
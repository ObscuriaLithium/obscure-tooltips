package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack

class AnyOfFilter(
    val terms: List<IItemFilter>
) : IItemFilter {

    override fun codec(): Codec<AnyOfFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return terms.any { it.test(stack) }
    }

    companion object {

        val CODEC: Codec<AnyOfFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                IItemFilter.CODEC.listOf()
                    .fieldOf("terms")
                    .forGetter(AnyOfFilter::terms)
            ).apply(codec, ::AnyOfFilter)
        }
    }
}
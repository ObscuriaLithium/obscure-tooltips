package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack

class NoneOfFilter(
    val terms: List<IItemFilter>
) : IItemFilter {

    override fun codec(): Codec<NoneOfFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return terms.none { it.test(stack) }
    }

    companion object {

        val CODEC: Codec<NoneOfFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                IItemFilter.CODEC.listOf()
                    .fieldOf("terms")
                    .forGetter(NoneOfFilter::terms)
            ).apply(codec, ::NoneOfFilter)
        }
    }
}
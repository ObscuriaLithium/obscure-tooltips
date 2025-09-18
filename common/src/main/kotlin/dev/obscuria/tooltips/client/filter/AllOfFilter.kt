package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack

class AllOfFilter(
    val terms: List<IItemFilter>
) : IItemFilter {

    override fun codec(): Codec<AllOfFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return terms.all { it.test(stack) }
    }

    companion object {

        val CODEC: Codec<AllOfFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                IItemFilter.CODEC.listOf()
                    .fieldOf("terms")
                    .forGetter(AllOfFilter::terms)
            ).apply(codec, ::AllOfFilter)
        }
    }
}
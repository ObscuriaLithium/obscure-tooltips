package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import net.minecraft.world.item.ItemStack

object AlwaysFilter : IItemFilter {

    val CODEC: Codec<AlwaysFilter> = Codec.unit(AlwaysFilter)

    override fun codec(): Codec<AlwaysFilter> = CODEC

    override fun test(stack: ItemStack): Boolean = true
}
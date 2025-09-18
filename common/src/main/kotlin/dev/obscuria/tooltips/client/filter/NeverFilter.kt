package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import net.minecraft.world.item.ItemStack

object NeverFilter : IItemFilter {

    val CODEC: Codec<NeverFilter> = Codec.unit(NeverFilter)

    override fun codec(): Codec<NeverFilter> = CODEC

    override fun test(stack: ItemStack): Boolean = false
}
package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack

class ModFilter(
    val modIds: List<String>
) : IItemFilter {

    override fun codec(): Codec<ModFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return modIds.contains(stack.item.builtInRegistryHolder().key().location().namespace)
    }

    companion object {

        val CODEC: Codec<ModFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.STRING.listOf()
                    .fieldOf("mod_ids")
                    .forGetter(ModFilter::modIds)
            ).apply(codec, ::ModFilter)
        }
    }
}
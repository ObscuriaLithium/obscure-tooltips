package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.ExtraCodecs.TagOrElementLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ItemFilter(
    val tags: List<TagKey<Item>>,
    val items: List<Item>
) : IItemFilter {

    override fun codec(): Codec<ItemFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return items.contains(stack.item) || tags.any { stack.`is`(it) }
    }

    fun pack(): List<TagOrElementLocation> {
        val packedTags = tags.map { TagOrElementLocation(it.location, true) }
        val packedItems = items.map { TagOrElementLocation(it.builtInRegistryHolder().key().location(), false) }
        return packedTags + packedItems
    }

    companion object {

        val CODEC: Codec<ItemFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                ExtraCodecs.TAG_OR_ELEMENT_ID.listOf()
                    .fieldOf("items")
                    .forGetter(ItemFilter::pack)
            ).apply(codec, ::unpack)
        }

        fun unpack(packed: List<TagOrElementLocation>): ItemFilter {
            val tags = mutableListOf<TagKey<Item>>()
            val items = mutableListOf<Item>()
            packed.forEach {
                if (it.tag) tags += TagKey.create(Registries.ITEM, it.id)
                else items += BuiltInRegistries.ITEM.get(it.id)
            }
            return ItemFilter(tags, items)
        }
    }
}
package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import java.util.Optional

class RarityFilter(
    val rarity: Rarity
): IItemFilter {

    override fun codec(): Codec<RarityFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return stack.rarity.equals(rarity)
    }

    companion object {

        private val RARITY_CODEC: Codec<Rarity> = Codec.STRING.comapFlatMap(::rarityByName, ::nameOfRarity)
        val CODEC: Codec<RarityFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                RARITY_CODEC
                    .fieldOf("rarity")
                    .forGetter(RarityFilter::rarity)
            ).apply(codec, ::RarityFilter)
        }

        private fun rarityByName(name: String): DataResult<Rarity> {
            return Optional.ofNullable(Rarity.entries.firstOrNull { it.name.lowercase() == name.lowercase() })
                .map { DataResult.success(it) }
                .orElseGet { DataResult.error { "Unknown rarity: $name" } }
        }

        private fun nameOfRarity(rarity: Rarity): String {
            return rarity.name.lowercase()
        }
    }
}
package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class EnchantmentFilter(
    val anyEnchantment: Optional<Boolean>,
    val anyCurse: Optional<Boolean>,
    val enchantments: Optional<List<Enchantment>>
) : IItemFilter {

    override fun codec(): Codec<EnchantmentFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        if (anyEnchantment.getOrNull()?.equals(stack.isEnchanted) == false) return false
        if (anyCurse.getOrNull()?.equals(stack.isCursed()) == false) return false
        if (enchantments.getOrNull()?.containsIn(stack) == false) return false
        return true
    }

    companion object {

        val CODEC: Codec<EnchantmentFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.BOOL
                    .optionalFieldOf("any_enchantment")
                    .forGetter(EnchantmentFilter::anyEnchantment),
                Codec.BOOL
                    .optionalFieldOf("any_curse")
                    .forGetter(EnchantmentFilter::anyCurse),
                BuiltInRegistries.ENCHANTMENT.byNameCodec().listOf()
                    .optionalFieldOf("enchantments")
                    .forGetter(EnchantmentFilter::enchantments)
            ).apply(codec, ::EnchantmentFilter)
        }

        fun ItemStack.isCursed(): Boolean {
            return EnchantmentHelper.getEnchantments(this).any { it.key.isCurse }
        }

        fun List<Enchantment>.containsIn(stack: ItemStack): Boolean {
            return this.all { EnchantmentHelper.getItemEnchantmentLevel(it, stack) > 0 }
        }
    }
}
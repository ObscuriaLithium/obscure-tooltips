package dev.obscuria.tooltips.client.filter

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.world.item.ItemStack
import java.util.function.Function

interface IItemFilter {

    fun codec(): Codec<out IItemFilter>

    fun test(stack: ItemStack): Boolean

    companion object {

        val CODEC: Codec<IItemFilter> = TooltipsRegistries.ITEM_FILTER_TYPE.byNameCodec.dispatch(IItemFilter::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out IItemFilter>) -> Any) {

            registrar("always", AlwaysFilter::CODEC)
            registrar("never", NeverFilter::CODEC)
            registrar("all_of", AllOfFilter::CODEC)
            registrar("any_of", AnyOfFilter::CODEC)
            registrar("none_of", NoneOfFilter::CODEC)
            registrar("item", ItemFilter::CODEC)
            registrar("mod", ModFilter::CODEC)
            registrar("enchantment", EnchantmentFilter::CODEC)
            registrar("rarity", RarityFilter::CODEC)
            registrar("nbt", NbtFilter::CODEC)
            registrar("property", PropertyFilter::CODEC)
        }
    }
}
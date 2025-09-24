package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ItemFilter {

    Codec<ItemFilter> CODEC = TooltipsRegistries.ITEM_FILTER_TYPE.byNameCodec().dispatch(ItemFilter::codec, Function.identity());

    Codec<? extends ItemFilter> codec();

    boolean test(ItemStack stack);

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends ItemFilter>>> registrar) {

        registrar.accept("always", () -> AlwaysFilter.CODEC);
        registrar.accept("never", () -> NeverFilter.CODEC);
        registrar.accept("all_of", () -> AllOfFilter.CODEC);
        registrar.accept("any_of", () -> AnyOfFilter.CODEC);
        registrar.accept("none_of", () -> NoneOfFilter.CODEC);
        registrar.accept("item", () -> ItemOrTagFilter.CODEC);
        registrar.accept("mod", () -> ModFilter.CODEC);
        registrar.accept("enchantment", () -> EnchantmentFilter.CODEC);
        registrar.accept("rarity", () -> RarityFilter.CODEC);
        registrar.accept("nbt", () -> NbtFilter.CODEC);
        registrar.accept("property", () -> PropertyFilter.CODEC);
    }
}

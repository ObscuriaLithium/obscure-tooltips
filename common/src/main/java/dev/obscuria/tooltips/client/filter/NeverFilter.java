package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

public record NeverFilter() implements ItemFilter {

    public static final NeverFilter INSTANCE = new NeverFilter();
    public static final Codec<NeverFilter> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<NeverFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }
}

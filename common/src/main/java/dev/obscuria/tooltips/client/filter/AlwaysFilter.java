package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

public record AlwaysFilter() implements ItemFilter {

    public static final AlwaysFilter INSTANCE = new AlwaysFilter();
    public static final Codec<AlwaysFilter> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<AlwaysFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return true;
    }
}

package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record AllOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static final Codec<AllOfFilter> CODEC;

    @Override
    public Codec<AllOfFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().allMatch(it -> it.test(stack));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ItemFilter.CODEC.listOf().fieldOf("terms").forGetter(AllOfFilter::terms)
        ).apply(codec, AllOfFilter::new));
    }
}

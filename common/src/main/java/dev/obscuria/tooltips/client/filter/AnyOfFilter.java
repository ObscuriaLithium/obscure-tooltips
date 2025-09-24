package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record AnyOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static final Codec<AnyOfFilter> CODEC;

    @Override
    public Codec<AnyOfFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().anyMatch(it -> it.test(stack));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ItemFilter.CODEC.listOf().fieldOf("terms").forGetter(AnyOfFilter::terms)
        ).apply(codec, AnyOfFilter::new));
    }
}

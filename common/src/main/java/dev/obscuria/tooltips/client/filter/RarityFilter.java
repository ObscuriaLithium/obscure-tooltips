package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record RarityFilter(String rarity) implements ItemFilter {

    public static final Codec<RarityFilter> CODEC;

    @Override
    public Codec<RarityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        final var rarity = stack.getRarity().name().toLowerCase();
        return this.rarity.equals(rarity);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.STRING.fieldOf("rarity").forGetter(RarityFilter::rarity)
        ).apply(codec, RarityFilter::new));
    }
}

package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record PropertyFilter(Optional<Boolean> hasFoil) implements ItemFilter {

    public static final Codec<PropertyFilter> CODEC;

    @Override
    public Codec<PropertyFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (hasFoil.isPresent() && !hasFoil.get().equals(stack.hasFoil())) return false;
        return true;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.optionalFieldOf("has_foil").forGetter(PropertyFilter::hasFoil)
        ).apply(codec, PropertyFilter::new));
    }
}

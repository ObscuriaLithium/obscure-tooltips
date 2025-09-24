package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ModFilter(List<String> mods) implements ItemFilter {

    public static final Codec<ModFilter> CODEC;

    @Override
    public Codec<ModFilter> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean test(ItemStack stack) {
        return mods.contains(stack.getItem().builtInRegistryHolder().key().location().getNamespace());
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.STRING.listOf().fieldOf("mods").forGetter(ModFilter::mods)
        ).apply(codec, ModFilter::new));
    }
}

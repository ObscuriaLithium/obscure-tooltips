package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;
import java.util.Optional;

public record EnchantmentFilter(
        Optional<Boolean> anyEnchantment,
        Optional<Boolean> anyCurse,
        Optional<List<Enchantment>> enchantments
) implements ItemFilter {

    public static final Codec<EnchantmentFilter> CODEC;

    @Override
    public Codec<EnchantmentFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (anyEnchantment.isPresent() && !anyEnchantment.get().equals(stack.isEnchanted())) return false;
        if (anyCurse.isPresent() && !anyCurse.get().equals(isCursed(stack))) return false;
        if (enchantments.isPresent() && !containsAll(stack, enchantments.get())) return false;
        return true;
    }

    private boolean isCursed(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).keySet().stream().anyMatch(Enchantment::isCurse);
    }

    private boolean containsAll(ItemStack stack, List<Enchantment> enchantments) {
        return enchantments.stream().allMatch(it -> EnchantmentHelper.getItemEnchantmentLevel(it, stack) > 0);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.optionalFieldOf("any_enchantment").forGetter(EnchantmentFilter::anyEnchantment),
                Codec.BOOL.optionalFieldOf("any_curse").forGetter(EnchantmentFilter::anyCurse),
                BuiltInRegistries.ENCHANTMENT.byNameCodec().listOf().optionalFieldOf("enchantments").forGetter(EnchantmentFilter::enchantments)
        ).apply(codec, EnchantmentFilter::new));
    }
}

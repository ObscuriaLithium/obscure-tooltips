package com.obscuria.tooltips.client;

import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class ItemGroups {
    public static final Predicate<ItemStack> RARE = stack -> rarityName(stack).equals("RARE");
    public static final Predicate<ItemStack> UNCOMMON_RARE = stack -> {
        final String rarity = rarityName(stack);
        return rarity.equals("UNCOMMON") || rarity.equals("RARE");
    };
    public static  final Predicate<ItemStack> EPIC_LEGENDARY_MYTHIC = stack -> {
        final String rarity = rarityName(stack);
        return rarity.equals("EPIC") || rarity.equals("LEGENDARY") || rarity.equals("MYTHIC");
    };
    public static final Predicate<ItemStack> CURSED = stack -> stack.getAllEnchantments().keySet().stream().anyMatch(Enchantment::isCurse);
    public static final Predicate<ItemStack> ENCHANTED = stack -> stack.getItem() instanceof EnchantedBookItem || stack.isEnchanted();
    public static final Predicate<ItemStack> ENDER = stack -> {
        final Item item = stack.getItem();
        return item instanceof EnderpearlItem
                || item instanceof EnderEyeItem
                || item instanceof ChorusFruitItem
                || item == Items.END_CRYSTAL
                || item == Items.DRAGON_BREATH
                || item == Items.DRAGON_EGG
                || item == Items.SHULKER_SHELL
                || item == Items.SHULKER_BOX
                || item == Items.POPPED_CHORUS_FRUIT;
    };

    private static String rarityName(ItemStack stack) {
        return stack.getRarity().toString().toUpperCase();
    }
}

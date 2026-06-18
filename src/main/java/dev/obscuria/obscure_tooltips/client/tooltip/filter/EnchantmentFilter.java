package dev.obscuria.obscure_tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Desugar
public record EnchantmentFilter(Boolean anyEnchantment, Boolean anyCurse, List<ResourceLocation> enchantments)
        implements ItemFilter {

    public static EnchantmentFilter fromJson(JsonObject json) {
        final Boolean anyEnchantment = JsonUtils.hasField(json, "any_enchantment")
                ? JsonUtils.getBoolean(json, "any_enchantment") : null;
        final Boolean anyCurse = JsonUtils.hasField(json, "any_curse")
                ? JsonUtils.getBoolean(json, "any_curse") : null;
        List<ResourceLocation> enchantments = null;
        if (JsonUtils.hasField(json, "enchantments")) {
            enchantments = new ArrayList<>();
            for (JsonElement element : JsonUtils.getJsonArray(json, "enchantments")) {
                enchantments.add(new ResourceLocation(element.getAsString()));
            }
        }
        return new EnchantmentFilter(anyEnchantment, anyCurse, enchantments);
    }

    @Override
    public boolean test(ItemStack stack) {
        if (anyEnchantment != null && anyEnchantment != stack.isItemEnchanted()) {
            return false;
        }
        if (anyCurse != null && anyCurse != isCursed(stack)) {
            return false;
        }
        return enchantments == null || containsAll(stack, enchantments);
    }

    private static boolean isCursed(ItemStack stack) {
        return readEnchantments(stack, "ench").stream().anyMatch(EnchantmentFilter::isCurse)
                || readEnchantments(stack, "StoredEnchantments").stream().anyMatch(EnchantmentFilter::isCurse);
    }

    private static boolean containsAll(ItemStack stack, List<ResourceLocation> enchantmentIds) {
        final List<Enchantment> present = new ArrayList<>(EnchantmentHelper.getEnchantments(stack).keySet());
        return enchantmentIds.stream()
                .map(Enchantment.REGISTRY::getObject)
                .allMatch(present::contains);
    }

    private static boolean isCurse(Enchantment enchantment) {
        final ResourceLocation id = Enchantment.REGISTRY.getNameForObject(enchantment);
        return id != null && id.getPath().toLowerCase(Locale.ROOT).contains("curse");
    }

    private static List<Enchantment> readEnchantments(ItemStack stack, String tagName) {
        final List<Enchantment> result = new ArrayList<>();
        final NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey(tagName, 9)) { // 9 = TAG_LIST
            return result;
        }
        final NBTTagList list = tag.getTagList(tagName, 10); // 10 = TAG_COMPOUND
        for (int i = 0; i < list.tagCount(); i++) {
            final Enchantment enchantment = Enchantment.getEnchantmentByID(list.getCompoundTagAt(i).getShort("id"));
            if (enchantment != null) {
                result.add(enchantment);
            }
        }
        return result;
    }
}

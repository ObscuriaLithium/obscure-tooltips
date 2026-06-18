package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nullable;

@Desugar
public record NbtFilter(NBTTagCompound nbt, boolean matchExact) implements ItemFilter {

    public static NbtFilter fromJson(JsonObject json) {
        final NBTTagCompound nbt;
        try {
            nbt = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "nbt"));
        } catch (NBTException exception) {
            throw new JsonParseException("Invalid nbt: " + exception.getMessage());
        }
        return new NbtFilter(nbt, JsonUtils.getBoolean(json, "match_exact", false));
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return isSubtagOf(nbt, stack.getTagCompound(), matchExact);
    }

    private static boolean isSubtagOf(@Nullable NBTBase tag, @Nullable NBTBase parent, boolean exactLists) {
        if (tag == null) {
            return true;
        }
        if (parent == null || tag.getId() != parent.getId()) {
            return false;
        }
        if (tag instanceof NBTTagCompound compound && parent instanceof NBTTagCompound parentCompound) {
            for (String key : compound.getKeySet()) {
                if (!isSubtagOf(compound.getTag(key), parentCompound.getTag(key), exactLists)) {
                    return false;
                }
            }
            return true;
        }
        if (tag instanceof NBTTagList list && parent instanceof NBTTagList parentList) {
            if (exactLists) {
                return list.equals(parentList);
            }
            if (list.tagCount() > parentList.tagCount()) {
                return false;
            }
            for (int i = 0; i < list.tagCount(); i++) {
                final NBTBase element = list.get(i);
                boolean found = false;
                for (int j = 0; j < parentList.tagCount(); j++) {
                    if (isSubtagOf(element, parentList.get(j), exactLists)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }
        return tag.equals(parent);
    }
}

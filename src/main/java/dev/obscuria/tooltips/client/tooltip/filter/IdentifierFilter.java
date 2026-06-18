package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;
import java.util.function.Function;

@Desugar
public record IdentifierFilter(TargetType target, String contains, String startsWith, String endsWith)
        implements ItemFilter {

    public static IdentifierFilter fromJson(JsonObject json) {
        final TargetType target = TargetType.byName(JsonUtils.getString(json, "target", "path"));
        final String contains = JsonUtils.getString(json, "contains", null);
        final String startsWith = JsonUtils.getString(json, "starts_with", null);
        final String endsWith = JsonUtils.getString(json, "ends_with", null);
        return new IdentifierFilter(target, contains, startsWith, endsWith);
    }

    @Override
    public boolean test(ItemStack stack) {
        final ResourceLocation id = stack.getItem().getRegistryName();
        if (id == null) {
            return false;
        }
        final String value = target.resolve(id);
        if (contains != null && !value.contains(contains)) {
            return false;
        }
        if (startsWith != null && !value.startsWith(startsWith)) {
            return false;
        }
        return endsWith == null || value.endsWith(endsWith);
    }

    public enum TargetType {
        PATH(ResourceLocation::getPath),
        NAMESPACE(ResourceLocation::getNamespace),
        FULL(ResourceLocation::toString);

        private final Function<ResourceLocation, String> extractor;

        TargetType(Function<ResourceLocation, String> extractor) {
            this.extractor = extractor;
        }

        public String resolve(ResourceLocation id) {
            return extractor.apply(id);
        }

        public static TargetType byName(String name) {
            for (TargetType type : values()) {
                if (type.name().toLowerCase(Locale.ROOT).equals(name)) {
                    return type;
                }
            }
            return PATH;
        }
    }
}

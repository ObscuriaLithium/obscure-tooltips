package dev.obscuria.obscure_tooltips.config;

import java.util.function.Supplier;

public enum BooleanDelegate {

    UNCOMMON_STYLE_ENABLED(ClientConfig.UNCOMMON_STYLE_ENABLED::get),
    RARE_STYLE_ENABLED(ClientConfig.RARE_STYLE_ENABLED::get),
    EPIC_STYLE_ENABLED(ClientConfig.EPIC_STYLE_ENABLED::get),
    ENCHANTED_STYLE_ENABLED(ClientConfig.ENCHANTED_STYLE_ENABLED::get),
    CURSED_STYLE_ENABLED(ClientConfig.CURSED_STYLE_ENABLED::get);

    private final Supplier<Boolean> valueProvider;

    BooleanDelegate(Supplier<Boolean> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public boolean value() {
        return this.valueProvider.get();
    }

    public String getSerializedName() {
        return "@" + name();
    }

    public static BooleanDelegate byName(String serialized) {
        final String name = serialized.startsWith("@") ? serialized.substring(1) : serialized;
        for (BooleanDelegate delegate : values()) {
            if (delegate.name().equals(name)) {
                return delegate;
            }
        }
        return null;
    }
}

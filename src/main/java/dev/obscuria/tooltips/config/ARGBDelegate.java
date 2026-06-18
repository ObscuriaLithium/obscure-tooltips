package dev.obscuria.tooltips.config;

import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.util.color.ARGB;
import dev.obscuria.tooltips.util.color.Colors;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public enum ARGBDelegate {

    DEFAULT_PANEL_BACKGROUND_TOP(ClientConfig.DEFAULT_PANEL_BACKGROUND_TOP::get),
    DEFAULT_PANEL_BACKGROUND_BOTTOM(ClientConfig.DEFAULT_PANEL_BACKGROUND_BOTTOM::get),
    DEFAULT_PANEL_BORDER_TOP(ClientConfig.DEFAULT_PANEL_BORDER_TOP::get),
    DEFAULT_PANEL_BORDER_BOTTOM(ClientConfig.DEFAULT_PANEL_BORDER_BOTTOM::get),
    UNCOMMON_PANEL_BACKGROUND_TOP(ClientConfig.UNCOMMON_PANEL_BACKGROUND_TOP::get),
    UNCOMMON_PANEL_BACKGROUND_BOTTOM(ClientConfig.UNCOMMON_PANEL_BACKGROUND_BOTTOM::get),
    UNCOMMON_PANEL_BORDER_TOP(ClientConfig.UNCOMMON_PANEL_BORDER_TOP::get),
    UNCOMMON_PANEL_BORDER_BOTTOM(ClientConfig.UNCOMMON_PANEL_BORDER_BOTTOM::get),
    RARE_PANEL_BACKGROUND_TOP(ClientConfig.RARE_PANEL_BACKGROUND_TOP::get),
    RARE_PANEL_BACKGROUND_BOTTOM(ClientConfig.RARE_PANEL_BACKGROUND_BOTTOM::get),
    RARE_PANEL_BORDER_TOP(ClientConfig.RARE_PANEL_BORDER_TOP::get),
    RARE_PANEL_BORDER_BOTTOM(ClientConfig.RARE_PANEL_BORDER_BOTTOM::get),
    EPIC_PANEL_BACKGROUND_TOP(ClientConfig.EPIC_PANEL_BACKGROUND_TOP::get),
    EPIC_PANEL_BACKGROUND_BOTTOM(ClientConfig.EPIC_PANEL_BACKGROUND_BOTTOM::get),
    EPIC_PANEL_BORDER_TOP(ClientConfig.EPIC_PANEL_BORDER_TOP::get),
    EPIC_PANEL_BORDER_BOTTOM(ClientConfig.EPIC_PANEL_BORDER_BOTTOM::get),
    EPIC_RAY_GLOW_PRIMARY(ClientConfig.EPIC_RAY_GLOW_PRIMARY::get),
    EPIC_RAY_GLOW_SECONDARY(ClientConfig.EPIC_RAY_GLOW_SECONDARY::get),
    EPIC_SHIMMER_INNER(ClientConfig.EPIC_SHIMMER_INNER::get),
    EPIC_SHIMMER_OUTER(ClientConfig.EPIC_SHIMMER_OUTER::get),
    EPIC_SHIMMER_ACCENT(ClientConfig.EPIC_SHIMMER_ACCENT::get),
    ENCHANTED_PARTICLE_CENTER(ClientConfig.ENCHANTED_PARTICLE_CENTER::get),
    ENCHANTED_PARTICLE_EDGE(ClientConfig.ENCHANTED_PARTICLE_EDGE::get),
    ENCHANTED_GLINT_PRIMARY_WAVE(ClientConfig.ENCHANTED_GLINT_PRIMARY_WAVE::get),
    ENCHANTED_GLINT_PRIMARY_WAVE_GLOW(ClientConfig.ENCHANTED_GLINT_PRIMARY_WAVE_GLOW::get),
    ENCHANTED_GLINT_SECONDARY_WAVE(ClientConfig.ENCHANTED_GLINT_SECONDARY_WAVE::get),
    ENCHANTED_GLINT_SECONDARY_WAVE_GLOW(ClientConfig.ENCHANTED_GLINT_SECONDARY_WAVE_GLOW::get),
    ENCHANTED_GLINT_RING(ClientConfig.ENCHANTED_GLINT_RING::get),
    CURSED_PARTICLE_CENTER(ClientConfig.CURSED_PARTICLE_CENTER::get),
    CURSED_PARTICLE_EDGE(ClientConfig.CURSED_PARTICLE_EDGE::get),
    CURSED_GLINT_PRIMARY_WAVE(ClientConfig.CURSED_GLINT_PRIMARY_WAVE::get),
    CURSED_GLINT_PRIMARY_WAVE_GLOW(ClientConfig.CURSED_GLINT_PRIMARY_WAVE_GLOW::get),
    CURSED_GLINT_SECONDARY_WAVE(ClientConfig.CURSED_GLINT_SECONDARY_WAVE::get),
    CURSED_GLINT_SECONDARY_WAVE_GLOW(ClientConfig.CURSED_GLINT_SECONDARY_WAVE_GLOW::get),
    CURSED_GLINT_RING(ClientConfig.CURSED_GLINT_RING::get);

    public static final ARGB FALLBACK = Colors.argbOf("#FFFFFFFF");

    private final Supplier<String> valueProvider;
    private final AtomicReference<String> lastValue = new AtomicReference<>("");
    private final AtomicReference<ARGB> cachedColor = new AtomicReference<>(Colors.argbOf("#FFFFFFFF"));

    ARGBDelegate(Supplier<String> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public ARGB color() {
        final String value = valueProvider.get();
        if (!value.equals(lastValue.get())) {
            lastValue.set(value);
            try {
                cachedColor.set(Colors.argbOf(value));
            } catch (Exception exception) {
                ObscureTooltips.LOGGER.warn("Invalid color value `{}` in config entry `{}`. Falling back to default color.",
                        value, getSerializedName(), exception);
                cachedColor.set(FALLBACK);
            }
        }
        return cachedColor.get();
    }

    public String getSerializedName() {
        return "@" + name();
    }

    public static ARGBDelegate byName(String serialized) {
        final String name = serialized.startsWith("@") ? serialized.substring(1) : serialized;
        for (ARGBDelegate delegate : values()) {
            if (delegate.name().equals(name)) {
                return delegate;
            }
        }
        return null;
    }
}

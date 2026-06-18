package dev.obscuria.tooltips.client.tooltip.element;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.config.ARGBProvider;
import dev.obscuria.tooltips.util.color.ARGB;

@Desugar
public record QuadPalette(ARGBProvider topLeftProvider, ARGBProvider topRightProvider,
                          ARGBProvider bottomLeftProvider, ARGBProvider bottomRightProvider) {

    public static QuadPalette fromJson(JsonObject json) {
        return new QuadPalette(
                ARGBProvider.fromJson(json.get("top_left")),
                ARGBProvider.fromJson(json.get("top_right")),
                ARGBProvider.fromJson(json.get("bottom_left")),
                ARGBProvider.fromJson(json.get("bottom_right")));
    }

    public ARGB topLeft() {
        return this.topLeftProvider.get();
    }

    public ARGB topRight() {
        return this.topRightProvider.get();
    }

    public ARGB bottomLeft() {
        return this.bottomLeftProvider.get();
    }

    public ARGB bottomRight() {
        return this.bottomRightProvider.get();
    }
}

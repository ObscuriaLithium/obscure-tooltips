package dev.obscuria.obscure_tooltips.util.color;

import com.github.bsideup.jabel.Desugar;
import lombok.With;

@Desugar
public record ARGB(
        @With float alpha,
        @With float red,
        @With float green,
        @With float blue)
{
    public ARGB lerp(ARGB to, float delta) {
        return new ARGB(
                lerp(delta, alpha, to.alpha),
                lerp(delta, red, to.red),
                lerp(delta, green, to.green),
                lerp(delta, blue, to.blue));
    }

    private static float lerp(float delta, float from, float to) {
        return from + delta * (to - from);
    }
}

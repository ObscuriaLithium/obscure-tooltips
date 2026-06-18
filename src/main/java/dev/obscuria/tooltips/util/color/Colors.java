package dev.obscuria.tooltips.util.color;

public final class Colors {
    public static ARGB argbOf(float alpha, float red, float green, float blue)
    {
        return new ARGB(alpha, red, green, blue);
    }

    public static ARGB argbOf(int decimal)
    {
        return new ARGB(
                ((decimal >> 24) & 0xFF) / 255f,
                ((decimal >> 16) & 0xFF) / 255f,
                ((decimal >> 8) & 0xFF) / 255f,
                (decimal & 0xFF) / 255f);
    }

    public static ARGB argbOf(String hexadecimal) {
        final String hex = hexadecimal.replace("#", "");
        return argbOf((int) Long.parseLong(hex, 16));
    }
}

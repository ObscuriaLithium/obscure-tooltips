package dev.obscuria.tooltips.client.tooltip.element;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.Vec3d;

@Desugar
public record Transform(
        Vec3d offset,
        float scale,
        float rotation) {

    public static final Transform DEFAULT = new Transform(Vec3d.ZERO, 1.0F, 0.0F);

    public static Transform fromJson(JsonObject json) {
        Vec3d offset = Vec3d.ZERO;
        if (json.has("offset")) {
            final JsonArray array = JsonUtils.getJsonArray(json, "offset");
            offset = new Vec3d(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
        }
        final float scale = JsonUtils.getFloat(json, "scale", 1.0F);
        final float rotation = JsonUtils.getFloat(json, "rotation", 0.0F);
        return new Transform(offset, scale, rotation);
    }

    public void apply(GuiGraphics graphics) {
        graphics.pose().translate((float) -offset.x, (float) -offset.y, (float) -offset.z);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().rotateZ(rotation);
    }
}

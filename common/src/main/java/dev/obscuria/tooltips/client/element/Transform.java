package dev.obscuria.tooltips.client.element;

import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;

public record Transform(
        Vec3 offset,
        float scale,
        float rotation) {

    public static final Transform DEFAULT = new Transform(Vec3.ZERO, 1f, 0f);
    public static final Codec<Transform> CODEC;

    public void apply(GuiGraphics graphics)
    {
        graphics.pose().translate(-offset.x, -offset.y, -offset.z);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(Transform::offset),
                Codec.FLOAT.optionalFieldOf("scale", 1f).forGetter(Transform::scale),
                Codec.FLOAT.optionalFieldOf("rotation", 0f).forGetter(Transform::rotation)
        ).apply(codec, Transform::new));
    }
}

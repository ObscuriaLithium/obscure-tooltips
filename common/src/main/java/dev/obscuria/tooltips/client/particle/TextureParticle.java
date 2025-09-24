package dev.obscuria.tooltips.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.element.Transform;
import dev.obscuria.tooltips.client.renderer.ParticleData;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record TextureParticle(
        ResourceLocation texture,
        Transform transform
) implements TooltipParticle {

    public static final Codec<TextureParticle> CODEC;

    @Override
    public Codec<TextureParticle> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, TooltipContext context, ParticleData data) {
        graphics.pose().pushPose();
        transform.apply(graphics);
        graphics.blit(texture, -8, -8, 0f, 0f, 16, 16, 16, 16);
        graphics.pose().popPose();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureParticle::texture),
                Transform.CODEC.optionalFieldOf("transform", Transform.DEFAULT).forGetter(TextureParticle::transform)
        ).apply(codec, TextureParticle::new));
    }
}

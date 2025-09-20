package dev.obscuria.tooltips.client.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.client.renderer.TooltipParticleData
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

class TextureParticle(
    val texture: ResourceLocation,
    val offset: Vec3,
    val scale: Float
) : ITooltipParticle {

    override fun codec(): Codec<TextureParticle> = CODEC

    override fun render(graphics: GuiGraphics, context: TooltipContext, data: TooltipParticleData) {
        graphics.pose().pushPose()
        graphics.pose().translate(-offset.x(), -offset.y(), -offset.z())
        graphics.pose().scale(scale, scale, scale)
        graphics.blit(texture, -8, -8, 0f, 0f, 16, 16, 16, 16)
        graphics.pose().popPose()
    }

    companion object {

        val CODEC: Codec<TextureParticle> = RecordCodecBuilder.create { codec ->
            codec.group(
                ResourceLocation.CODEC
                    .fieldOf("texture")
                    .forGetter(TextureParticle::texture),
                Vec3.CODEC
                    .optionalFieldOf("offset", Vec3.ZERO)
                    .forGetter(TextureParticle::offset),
                Codec.FLOAT
                    .optionalFieldOf("scale", 1f)
                    .forGetter(TextureParticle::scale)
            ).apply(codec, ::TextureParticle)
        }
    }
}
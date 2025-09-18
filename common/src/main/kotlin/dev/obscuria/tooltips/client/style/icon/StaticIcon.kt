package dev.obscuria.tooltips.client.style.icon

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.extension.GraphicsExtensions.scale
import dev.obscuria.tooltips.extension.GraphicsExtensions.translate
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.jvm.optionals.getOrDefault

class StaticIcon(
    val offset: Optional<Vec3>,
    val scale: Optional<Float>
) : ITooltipIcon {

    override fun codec(): Codec<out ITooltipIcon> = CODEC

    override fun render(graphics: GuiGraphics, stack: ItemStack, x: Int, y: Int) {
        graphics.pose().pushPose()
        graphics.pose().translate(x + 8f, y + 8f, 0f)
        graphics.pose().translate(this.offset.getOrDefault(Vec3.ZERO))
        graphics.pose().scale(this.scale.getOrDefault(1f))
        graphics.renderItem(stack, -8, -8)
        graphics.pose().popPose()
    }

    companion object {

        val CODEC: Codec<StaticIcon> = RecordCodecBuilder.create { codec ->
            codec.group(
                Vec3.CODEC
                    .optionalFieldOf("offset")
                    .forGetter(StaticIcon::offset),
                Codec.FLOAT
                    .optionalFieldOf("scale")
                    .forGetter(StaticIcon::scale)
            ).apply(codec, ::StaticIcon)
        }
    }
}
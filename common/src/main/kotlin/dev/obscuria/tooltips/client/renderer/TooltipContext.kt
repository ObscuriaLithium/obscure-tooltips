package dev.obscuria.tooltips.client.renderer

import dev.obscuria.tooltips.client.TooltipLabel
import dev.obscuria.tooltips.client.TooltipStyle
import net.minecraft.Util
import net.minecraft.world.item.ItemStack

class TooltipContext(
    val stack: ItemStack = ItemStack.EMPTY,
    val style: TooltipStyle = TooltipStyle.EMPTY,
    val label: TooltipLabel? = null
) {

    val startTime: Long = Util.getMillis()
    val particles: MutableList<TooltipParticleData> = mutableListOf()

    fun timeInSeconds(): Float {
        return (Util.getMillis() - startTime) * 0.001f
    }

    fun addParticle(particle: TooltipParticleData) {
        particles.add(particle)
    }

    inline fun forEachParticle(source: Any, handler: (TooltipParticleData) -> Unit) {
        particles.forEach {
            if (it.source != source) return@forEach
            handler(it)
        }
    }

    internal fun removeExpiredParticles() {
        particles.removeIf {
            it.status == ParticleStatus.EXPIRED
        }
    }
}
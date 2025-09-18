package dev.obscuria.tooltips.registry

import com.mojang.serialization.Codec
import dev.obscuria.fragmentum.registry.FragmentumRegistry
import dev.obscuria.tooltips.ObscureTooltips
import dev.obscuria.tooltips.ObscureTooltips.key
import dev.obscuria.tooltips.client.filter.IItemFilter
import dev.obscuria.tooltips.client.style.effect.ITooltipEffect
import dev.obscuria.tooltips.client.style.frame.ITooltipFrame
import dev.obscuria.tooltips.client.style.icon.ITooltipIcon
import dev.obscuria.tooltips.client.style.panel.ITooltipPanel
import dev.obscuria.tooltips.client.style.slot.ITooltipSlot
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object TooltipsRegistries {

    private val REGISTRAR = FragmentumRegistry.registrar(ObscureTooltips.MODID)

    val ITEM_FILTER_TYPE = REGISTRAR.newRegistry(Keys.ITEM_FILTER_TYPE)
    val TOOLTIP_PANEL_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_PANEL_TYPE)
    val TOOLTIP_FRAME_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_FRAME_TYPE)
    val TOOLTIP_SLOT_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_SLOT_TYPE)
    val TOOLTIP_ICON_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_ICON_TYPE)
    val TOOLTIP_EFFECT_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_EFFECT_TYPE)

    object Keys {

        val ITEM_FILTER_TYPE = create<Codec<out IItemFilter>>("item_filter_type")
        val TOOLTIP_PANEL_TYPE = create<Codec<out ITooltipPanel>>("tooltip_panel_type")
        val TOOLTIP_FRAME_TYPE = create<Codec<out ITooltipFrame>>("tooltip_frame_type")
        val TOOLTIP_SLOT_TYPE = create<Codec<out ITooltipSlot>>("tooltip_slot_type")
        val TOOLTIP_ICON_TYPE = create<Codec<out ITooltipIcon>>("tooltip_icon_type")
        val TOOLTIP_EFFECT_TYPE = create<Codec<out ITooltipEffect>>("tooltip_effect_type")

        private fun <T> create(name: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(key(name))
    }

    internal fun init() {

        IItemFilter.bootstrap { name, codec -> REGISTRAR.register(Keys.ITEM_FILTER_TYPE, key(name), codec) }
        ITooltipPanel.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_PANEL_TYPE, key(name), codec) }
        ITooltipFrame.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_FRAME_TYPE, key(name), codec) }
        ITooltipSlot.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_SLOT_TYPE, key(name), codec) }
        ITooltipIcon.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_ICON_TYPE, key(name), codec) }
        ITooltipEffect.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_EFFECT_TYPE, key(name), codec) }
    }
}
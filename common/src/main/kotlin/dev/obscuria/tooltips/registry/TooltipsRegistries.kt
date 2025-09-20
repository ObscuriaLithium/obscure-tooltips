package dev.obscuria.tooltips.registry

import com.mojang.serialization.Codec
import dev.obscuria.fragmentum.registry.FragmentumRegistry
import dev.obscuria.tooltips.ObscureTooltips
import dev.obscuria.tooltips.ObscureTooltips.key
import dev.obscuria.tooltips.client.TooltipDefinition
import dev.obscuria.tooltips.client.TooltipLabel
import dev.obscuria.tooltips.client.TooltipStyle
import dev.obscuria.tooltips.client.filter.IItemFilter
import dev.obscuria.tooltips.client.element.effect.ITooltipEffect
import dev.obscuria.tooltips.client.element.frame.ITooltipFrame
import dev.obscuria.tooltips.client.element.icon.ITooltipIcon
import dev.obscuria.tooltips.client.element.panel.ITooltipPanel
import dev.obscuria.tooltips.client.element.slot.ITooltipSlot
import dev.obscuria.tooltips.client.label.ILabelProvider
import dev.obscuria.tooltips.client.particle.ITooltipParticle
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object TooltipsRegistries {

    private val REGISTRAR = FragmentumRegistry.registrar(ObscureTooltips.MODID)

    val ITEM_FILTER_TYPE = REGISTRAR.newRegistry(Keys.ITEM_FILTER_TYPE)
    val LABEL_PROVIDER_TYPE = REGISTRAR.newRegistry(Keys.LABEL_PROVIDER_TYPE)
    val TOOLTIP_PARTICLE_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_PARTICLE_TYPE)
    val TOOLTIP_PANEL_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_PANEL_TYPE)
    val TOOLTIP_FRAME_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_FRAME_TYPE)
    val TOOLTIP_SLOT_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_SLOT_TYPE)
    val TOOLTIP_ICON_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_ICON_TYPE)
    val TOOLTIP_EFFECT_TYPE = REGISTRAR.newRegistry(Keys.TOOLTIP_EFFECT_TYPE)

    object Resource {

        val TOOLTIP_PANEL = ResourceRegistry<ITooltipPanel>()
        val TOOLTIP_FRAME = ResourceRegistry<ITooltipFrame>()
        val TOOLTIP_SLOT = ResourceRegistry<ITooltipSlot>()
        val TOOLTIP_ICON = ResourceRegistry<ITooltipIcon>()
        val TOOLTIP_EFFECT = ResourceRegistry<ITooltipEffect>()
        val TOOLTIP_STYLE = ResourceRegistry<TooltipStyle>()
        val TOOLTIP_DEFINITION = ResourceRegistry.Ordered<TooltipDefinition>()
        val TOOLTIP_LABEL = ResourceRegistry.Ordered<TooltipLabel>()
    }

    object Keys {

        val ITEM_FILTER_TYPE = create<Codec<out IItemFilter>>("item_filter_type")
        val LABEL_PROVIDER_TYPE = create<Codec<out ILabelProvider>>("label_provider_type")
        val TOOLTIP_PARTICLE_TYPE = create<Codec<out ITooltipParticle>>("tooltip_particle_type")
        val TOOLTIP_PANEL_TYPE = create<Codec<out ITooltipPanel>>("tooltip_panel_type")
        val TOOLTIP_FRAME_TYPE = create<Codec<out ITooltipFrame>>("tooltip_frame_type")
        val TOOLTIP_SLOT_TYPE = create<Codec<out ITooltipSlot>>("tooltip_slot_type")
        val TOOLTIP_ICON_TYPE = create<Codec<out ITooltipIcon>>("tooltip_icon_type")
        val TOOLTIP_EFFECT_TYPE = create<Codec<out ITooltipEffect>>("tooltip_effect_type")

        private fun <T> create(name: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(key(name))
    }

    internal fun init() {

        IItemFilter.bootstrap { name, codec -> REGISTRAR.register(Keys.ITEM_FILTER_TYPE, key(name), codec) }
        ILabelProvider.bootstrap { name, codec -> REGISTRAR.register(Keys.LABEL_PROVIDER_TYPE, key(name), codec) }
        ITooltipParticle.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_PARTICLE_TYPE, key(name), codec) }
        ITooltipPanel.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_PANEL_TYPE, key(name), codec) }
        ITooltipFrame.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_FRAME_TYPE, key(name), codec) }
        ITooltipSlot.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_SLOT_TYPE, key(name), codec) }
        ITooltipIcon.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_ICON_TYPE, key(name), codec) }
        ITooltipEffect.bootstrap { name, codec -> REGISTRAR.register(Keys.TOOLTIP_EFFECT_TYPE, key(name), codec) }
    }
}
package dev.obscuria.tooltips.client.label;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record TranslatableLabelProvider(String key) implements LabelProvider {

    public static final Codec<TranslatableLabelProvider> CODEC;

    @Override
    public Codec<TranslatableLabelProvider> codec() {
        return CODEC;
    }

    @Override
    public ClientTooltipComponent create(ItemStack stack) {
        final var component = Component.translatable(key).withStyle(ChatFormatting.GRAY);
        return ClientTooltipComponent.create(component.getVisualOrderText());
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.STRING.fieldOf("key").forGetter(TranslatableLabelProvider::key)
        ).apply(codec, TranslatableLabelProvider::new));
    }
}

package dev.obscuria.tooltips.client.label;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface LabelProvider {

    Codec<LabelProvider> CODEC = TooltipsRegistries.LABEL_PROVIDER_TYPE.byNameCodec().dispatch(LabelProvider::codec, Function.identity());

    Codec<? extends LabelProvider> codec();

    ClientTooltipComponent create(ItemStack stack);

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends LabelProvider>>> registrar) {
        registrar.accept("literal", () -> LiteralLabelProvider.CODEC);
        registrar.accept("translatable", () -> TranslatableLabelProvider.CODEC);
        registrar.accept("rarity", () -> RarityLabelProvider.CODEC);
    }
}

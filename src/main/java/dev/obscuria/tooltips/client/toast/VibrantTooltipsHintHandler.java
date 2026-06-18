package dev.obscuria.tooltips.client.toast;

import dev.obscuria.obscure_tooltips.Tags;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class VibrantTooltipsHintHandler {
    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiMainMenu) || !ClientConfig.SHOW_VIBRANT_TOOLTIPS_HINT.get()) {
            return;
        }

        Minecraft.getMinecraft().getToastGui().add(new VibrantTooltipsHint());
        ClientConfig.disableVibrantTooltipsHint();
    }
}

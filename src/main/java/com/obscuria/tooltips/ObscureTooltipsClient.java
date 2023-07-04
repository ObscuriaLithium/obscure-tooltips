package com.obscuria.tooltips;

import com.obscuria.tooltips.client.ResourceLoader;
import com.obscuria.tooltips.client.TooltipsAssociations;
import com.obscuria.tooltips.registry.TooltipsRegistry;
import com.obscuria.tooltips.client.style.TooltipStylePreset;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.item.Items;

public class ObscureTooltipsClient {
    public static void setup() {
        TooltipsRegistry.setup();
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(ResourceLoader.INSTANCE);

        final TooltipStylePreset bones = new TooltipStylePreset.Builder().withFrame(TooltipsRegistry.BUILTIN_FRAME_BONES.get()).build();
        TooltipsAssociations.associate(Items.NETHERITE_SWORD, bones);
        TooltipsAssociations.associate(Items.ROTTEN_FLESH, bones);
        TooltipsAssociations.associate(Items.BONE, bones);
    }
}

package com.obscuria.obscuretooltips;

import com.obscuria.obscureapi.ObscureAPI;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    public static class Common {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec COMMON_SPEC;
        public static final ForgeConfigSpec.BooleanValue dropSharpRib;

        static {
            BUILDER.push("General");
            dropSharpRib = BUILDER.comment("Drop Sharp Rib from skeletons").worldRestart().define("dropSharpRib", true);
            BUILDER.pop();
            COMMON_SPEC = BUILDER.build();
        }
    }

    public static class Client {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec CLIENT_SPEC;
        public static final ForgeConfigSpec.BooleanValue displayFoodIcons;
        public static final ForgeConfigSpec.BooleanValue displayEquipmentIcons;
        public static final ForgeConfigSpec.BooleanValue reduceTooltips;
        public static final ForgeConfigSpec.IntValue buttonsOffsetX;
        public static final ForgeConfigSpec.BooleanValue pickupDisplay;
        public static final ForgeConfigSpec.BooleanValue pickupStyle;
        public static final ForgeConfigSpec.IntValue buttonsOffsetY;
        public static final ForgeConfigSpec.IntValue pickupLifetime;
        public static final ForgeConfigSpec.IntValue pickupOffset;

        static {
            BUILDER.push("PickupNotifier");
            pickupDisplay = BUILDER.worldRestart().define("Display", true);
            pickupStyle = BUILDER.worldRestart().define("Style", true);
            pickupLifetime = BUILDER.worldRestart().defineInRange("Lifetime", 5, 0, 20);
            pickupOffset = BUILDER.worldRestart().defineInRange("Offset", 0, 0, 1080);

            BUILDER.pop();
            BUILDER.push("Tooltips");
            displayFoodIcons = BUILDER.comment("Display food properties in tooltips").worldRestart().define("Food Icons", true);
            displayEquipmentIcons = BUILDER.comment("Display equipment attributes in tooltips").worldRestart().define("Equipment Icons", true);
            reduceTooltips = BUILDER.comment("Display reduced tooltips for new inventory buttons").worldRestart().define("Small Tooltips", true);
            buttonsOffsetX = BUILDER.worldRestart().defineInRange("Inventory Buttons Offset X", 0, -1000, 1000);
            buttonsOffsetY = BUILDER.worldRestart().defineInRange("Inventory Buttons Offset Y", 0, -1000, 1000);
            BUILDER.pop();
            CLIENT_SPEC = BUILDER.build();
        }
    }

    public static void init() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path bopConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "Obscuria");
        try {
            Files.createDirectory(bopConfigPath);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            ObscureAPI.LOGGER.error("Failed to create Obscuria config directory", e);
        }
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, Common.COMMON_SPEC,
                "Obscuria/obscure-api-common.toml");
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, Client.CLIENT_SPEC,
                "Obscuria/obscure-api-client.toml");
    }
}


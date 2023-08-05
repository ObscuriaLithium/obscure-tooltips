package com.obscuria.tooltips;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ObscureTooltipsConfig {

    public static class Client {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec CLIENT_SPEC;
        public static final ForgeConfigSpec.BooleanValue displayArmorModels;
        public static final ForgeConfigSpec.BooleanValue displayToolModels;

        static {
            BUILDER.push("Settings");
            displayArmorModels = BUILDER.worldRestart().define("displayArmorModels", true);
            displayToolModels = BUILDER.worldRestart().define("displayToolsModels", true);
            BUILDER.pop();
            CLIENT_SPEC = BUILDER.build();
        }
    }

    public static void setup() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "Obscuria");
        try { Files.createDirectory(modConfigPath); }
        catch (FileAlreadyExistsException ignored) {}
        catch (Exception e) { ObscureTooltips.LOGGER.error("Failed to create Obscuria config directory", e); }
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Client.CLIENT_SPEC, "Obscuria/obscure-tooltips-client.toml");
    }
}

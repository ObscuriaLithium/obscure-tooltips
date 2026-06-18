package dev.obscuria.tooltips.client.tooltip.element;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Desugar
public record SoundTemplate(ResourceLocation value, float volume, float pitch) {
    
    public static SoundTemplate fromJson(JsonObject json) {
        final ResourceLocation value = new ResourceLocation(JsonUtils.getString(json, "value"));
        final float volume = JsonUtils.getFloat(json, "volume", 1.0F);
        final float pitch = JsonUtils.getFloat(json, "pitch", 1.0F);
        return new SoundTemplate(value, volume, pitch);
    }

    public void play() {
        final SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(value);
        if (sound != null) {
            final float volume = this.volume * ClientConfig.SOUND_VOLUME.get().floatValue();
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(sound, pitch, volume));
        } else {
            ObscureTooltips.LOGGER.warn("Failed to play unknown sound event {}", value);
        }
    }
}

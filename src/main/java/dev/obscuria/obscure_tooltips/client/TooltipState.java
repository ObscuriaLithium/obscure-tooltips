package dev.obscuria.obscure_tooltips.client;

import dev.obscuria.obscure_tooltips.client.component.BlankComponent;
import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipDefinition;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipLabel;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipStyle;
import dev.obscuria.obscure_tooltips.client.tooltip.element.SoundTemplate;
import dev.obscuria.obscure_tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class TooltipState {
    private static UUID lastIconSoundUuid = UUID.randomUUID();
    private static long lastIconSoundTime = 0;

    public final ItemStack stack;
    public final TooltipStyle style;
    public final @Nullable TooltipLabel label;
    public final Long startTime;
    public final List<ParticleData> particles;
    public final UUID uuid = UUID.randomUUID();
    public boolean isFirstFrame = true;

    protected TooltipState(ItemStack stack) {
        this.stack = stack;
        this.style = TooltipDefinition.aggregateStyleFor(stack);
        this.label = TooltipLabel.findFor(stack);
        this.startTime = Minecraft.getSystemTime();
        this.particles = new ArrayList<>();
    }

    public boolean isInitialFrame() {
        return Minecraft.getSystemTime() == startTime;
    }

    public float timeInSeconds() {
        return (Minecraft.getSystemTime() - startTime) * 0.001f;
    }

    public TooltipComponent createLabel() {
        return label != null ? label.create(stack) : BlankComponent.INSTANCE;
    }

    public void addParticle(ParticleData particle) {
        particles.add(particle);
    }

    public void maybePlayIconSound(SoundTemplate template) {
        if (!ClientConfig.SOUNDS_ENABLED.get()) return;
        if (uuid.equals(lastIconSoundUuid)) return;
        if (Minecraft.getSystemTime() - startTime < 100) return;
        if (Minecraft.getSystemTime() - lastIconSoundTime < 300) return;
        lastIconSoundTime = Minecraft.getSystemTime();
        lastIconSoundUuid = uuid;
        template.play();
    }

    public void renderPanel(GuiGraphics graphics, int x, int y, int width, int height) {
        style.panel().ifPresent(panel -> panel.render(graphics, x, y, width, height));
    }

    public void renderEffects(GuiGraphics graphics, int x, int y, int width, int height) {
        for (var effect : style.effects()) {
            effect.renderBack(this, graphics, x, y, width, height);
        }
    }

    public void renderFrame(GuiGraphics graphics, int x, int y, int width, int height) {
        style.frame().ifPresent(frame -> frame.render(graphics, x, y, width, height));
    }

    public void update() {
        particles.removeIf(ParticleData::isExpired);
        isFirstFrame = false;
    }
}

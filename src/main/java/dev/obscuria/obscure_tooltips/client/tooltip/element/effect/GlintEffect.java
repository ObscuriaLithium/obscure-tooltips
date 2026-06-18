package dev.obscuria.obscure_tooltips.client.tooltip.element.effect;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.TooltipHelper;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.config.ARGBProvider;
import dev.obscuria.obscure_tooltips.util.color.ARGB;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.JsonUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@Desugar
public record GlintEffect(
        int segments,
        boolean clipWaves,
        boolean clipRings,
        List<WaveSpecs> waves,
        List<RingSpecs> rings
) implements TooltipEffect {

    private static final float TAU = (float) Math.PI * 2.0f;

    public static GlintEffect fromJson(JsonObject json) {
        final int segments = JsonUtils.getInt(json, "segments");
        final boolean clipWaves = JsonUtils.getBoolean(json, "clip_waves", true);
        final boolean clipRings = JsonUtils.getBoolean(json, "clip_rings", true);
        final List<WaveSpecs> waves = new ArrayList<>();
        for (JsonElement element : JsonUtils.getJsonArray(json, "waves")) {
            waves.add(WaveSpecs.fromJson(element.getAsJsonObject()));
        }
        final List<RingSpecs> rings = new ArrayList<>();
        for (JsonElement element : JsonUtils.getJsonArray(json, "rings")) {
            rings.add(RingSpecs.fromJson(element.getAsJsonObject()));
        }
        return new GlintEffect(segments, clipWaves, clipRings, waves, rings);
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof GlintEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.pose().pushMatrix();
        final var centerX = x + width * 0.5f;
        final var centerY = y + height * 0.5f;
        final var radius = (float) Math.hypot(width, height) * 0.85f;
        final var timer = state.timeInSeconds() * 0.1f;
        TooltipHelper.enableGlowingRenderer();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        if (clipWaves) {
            graphics.enableScissor(x - 3, y - 3, x + width + 3, y + height + 3);
        }
        renderWaves(buffer, centerX, centerY, width, height, radius, timer);
        if (clipWaves) {
            graphics.disableScissor();
        }
        if (clipRings) {
            graphics.enableScissor(x - 3, y - 3, x + width + 3, y + height + 3);
        }
        renderRings(buffer, centerX, centerY, radius, timer);
        if (clipRings) {
            graphics.disableScissor();
        }
        TooltipHelper.disableGlowingRenderer();
        graphics.pose().popMatrix();
    }

    private void renderWaves(BufferBuilder buffer, float x, float y, int width, int height, float radius, float timer) {
        if (waves.isEmpty()) return;
        final var aspect = height / (float) Math.max(1, width);

        for (var specs : waves) {
            final var color = specs.color.get();
            final var baseRadius = radius * specs.position();
            final var thickness = radius * specs.thickness() * 0.5f;
            final var progress = timer * TAU * specs.flowSpeed();

            buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (var i = 0; i <= segments; i++) {
                final var segment = i / (float) segments;
                final var angle = segment * TAU + progress;

                final var sin = (float) Math.sin(angle);
                final var cos = (float) Math.cos(angle);

                final var swirl = computeSwirl(angle, progress, specs.flowOffset()) * radius;
                final var ringRadius = baseRadius + swirl;

                final var innerR = ringRadius + thickness * specs.innerBias();
                final var outerR = ringRadius + thickness * specs.outerBias();

                final var ix = x + cos * innerR;
                final var iy = y + sin * innerR * aspect;
                final var ox = x + cos * outerR;
                final var oy = y + sin * outerR * aspect;

                final var intensity = computeIntensity(sin, angle, progress, specs.intensityOffset(), specs.verticalFade());

                final var innerA = color.alpha() * intensity * specs.innerAlpha();
                final var outerA = color.alpha() * intensity * specs.outerAlpha();

                buffer.pos(ox, oy, 0).color(color.red(), color.green(), color.blue(), outerA).endVertex();
                buffer.pos(ix, iy, 0).color(color.red(), color.green(), color.blue(), innerA).endVertex();
            }

            Tessellator.getInstance().draw();
        }
    }

    private void renderRings(BufferBuilder buffer, float x, float y, float radius, float timer) {
        if (rings.isEmpty()) return;

        for (var ringIndex = 0; ringIndex < rings.size(); ringIndex++) {

            final var specs = rings.get(ringIndex);
            final var color = specs.color.get();
            final var baseRadius = radius * specs.radius();
            final var thickness = radius * specs.thickness();
            final var progress = timer * TAU * specs.spinSpeed();
            final var arcSpan = 4.3982296f;

            buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (var i = 0; i <= segments; i++) {
                final var segment = i / (float) segments;
                final var angle = (segment - 0.5f) * arcSpan + progress + specs.arcOffset();
                final var sin = (float) Math.sin(angle);
                final var cos = (float) Math.cos(angle);

                final var wobble = computeWobble(segment, timer, ringIndex);
                final var rBase = baseRadius + wobble * radius * 0.18f;

                final var innerR = rBase - thickness * 0.5f;
                final var outerR = rBase + thickness * 0.5f;

                final var ix = x + cos * innerR;
                final var iy = y + sin * innerR;
                final var ox = x + cos * outerR;
                final var oy = y + sin * outerR;

                final var intensity = computeIntensity(segment, timer, ringIndex);

                final var edgeA = color.alpha() * intensity * 0.4f;
                final var coreA = color.alpha() * intensity * 0.9f;

                buffer.pos(ox, oy, 0.0f).color(color.red(), color.green(), color.blue(), edgeA).endVertex();
                buffer.pos(ix, iy, 0.0f).color(color.red(), color.green(), color.blue(), coreA).endVertex();
            }

            Tessellator.getInstance().draw();
        }
    }

    private float computeSwirl(float angle, float progress, float offset) {
        final var swirl1 = 0.12f * (float) Math.sin(angle * 3f + progress * 2.1f + offset * 0.7f);
        final var swirl2 = 0.07f * (float) Math.sin(angle * 7f - progress * 1.4f);
        return swirl1 + swirl2;
    }

    private float computeIntensity(float sin, float angle, float progress, float offset, boolean verticalFade) {
        final var band = 0.5f + 0.5f * (float) Math.sin(angle * 2f - progress * 1.3f + offset);
        if (verticalFade) {
            final var vertical = Math.max(0f, sin * 0.8f + 0.2f);
            return vertical * (0.4f + 0.6f * band);
        } else {
            final var hMask = (float) Math.pow(Math.abs(sin), 1.4f);
            return hMask * (0.35f + 0.65f * band);
        }
    }

    private float computeWobble(float segment, float timer, int ringIndex) {
        return 0.08f * (float) Math.sin(segment * 6f + timer * TAU * 2f + ringIndex * 1.7f);
    }

    private float computeIntensity(float segment, float timer, int ringIndex) {
        final var arcMask = (float) Math.sin(segment * Math.PI);
        final var pulse = 0.6f + 0.4f * (float) Math.sin(timer * TAU * 3f + segment * 5f + ringIndex * 1.3f);
        return arcMask * pulse;
    }

    @Desugar
    public record WaveSpecs(ARGBProvider color, float position, float thickness, float innerAlpha, float innerBias,
                            float outerAlpha, float outerBias, float flowSpeed, float flowOffset, float intensityOffset,
                            boolean verticalFade) {

        public static WaveSpecs fromJson(JsonObject json) {
            return new WaveSpecs(
                    ARGBProvider.fromJson(json.get("color")),
                    JsonUtils.getFloat(json, "position"),
                    JsonUtils.getFloat(json, "thickness"),
                    JsonUtils.getFloat(json, "inner_alpha"),
                    JsonUtils.getFloat(json, "inner_bias"),
                    JsonUtils.getFloat(json, "outer_alpha"),
                    JsonUtils.getFloat(json, "outer_bias"),
                    JsonUtils.getFloat(json, "flow_speed"),
                    JsonUtils.getFloat(json, "flow_offset"),
                    JsonUtils.getFloat(json, "intensity_offset"),
                    JsonUtils.getBoolean(json, "vertical_fade"));
        }
    }

    @Desugar
    public record RingSpecs(ARGBProvider color, float spinSpeed, float radius, float thickness, float arcOffset) {

        public static RingSpecs fromJson(JsonObject json) {
            return new RingSpecs(
                    ARGBProvider.fromJson(json.get("color")),
                    JsonUtils.getFloat(json, "spin_speed"),
                    JsonUtils.getFloat(json, "radius"),
                    JsonUtils.getFloat(json, "thickness"),
                    JsonUtils.getFloat(json, "arc_offset"));
        }
    }
}

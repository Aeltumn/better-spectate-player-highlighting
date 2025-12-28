package com.aeltumn.bsph.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

/**
 * Stores the option instances for the various options.
 */
public class GlowingOptions {
    public static final OptionInstance<ActivationMode> ACTIVATION_MODE = new OptionInstance<>(
            "bsph.options.activation_mode.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("bsph.options.activation_mode.tooltip")),
            GlowingOptions::activationModeLabel,
            new OptionInstance.Enum<>(
                    Arrays.asList(ActivationMode.values()),
                    Codec.STRING.xmap(ActivationMode::valueOf, ActivationMode::name)),
            GlowingConfig.get().activationMode,
            (newValue) -> {
                var config = GlowingConfig.get();
                config.activationMode = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> SHOW_GLOWING = OptionInstance.createBoolean(
            "bsph.options.show_glowing.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("bsph.options.show_glowing.tooltip")),
            GlowingConfig.get().showGlowing,
            (newValue) -> {
                var config = GlowingConfig.get();
                config.showGlowing = newValue;
                config.save();
            });

    public static final OptionInstance<Boolean> SHOW_INVISIBILITY = OptionInstance.createBoolean(
            "bsph.options.show_invisibility.name",
            OptionInstance.cachedConstantTooltip(
                    Component.translatable("bsph.options.show_invisibility.tooltip")),
            GlowingConfig.get().showInvisibility,
            (newValue) -> {
                var config = GlowingConfig.get();
                config.showInvisibility = newValue;
                config.save();
            });

    private static Component activationModeLabel(Component component, Enum<?> e) {
        return Component.translatable("bsph.options.activation_mode." + e.name().toLowerCase());
    }
}

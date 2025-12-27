package com.aeltumn.bsph.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.fabricmc.fabric.impl.resource.pack.ModPackResourcesUtil.GSON;

public class GlowingConfig {
    private static GlowingConfig instance;

    public ActivationMode activationMode = ActivationMode.TOGGLE;
    public boolean showGlowing = true;
    public boolean showInvisibility = true;

    /**
     * Returns the current config state.
     */
    public static GlowingConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * Loads this configuration file.
     */
    private static GlowingConfig load() {
        var file = getConfigFile();
        if (Files.exists(file)) {
            try (var reader = new FileReader(file.toFile())) {
                return GSON.fromJson(reader, GlowingConfig.class);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return new GlowingConfig();
    }

    /**
     * Saves the changes to this configuration file.
     */
    public void save() {
        try {
            Files.writeString(getConfigFile(), GSON.toJson(this));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Returns the path where the configuration file is stored.
     */
    public static Path getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("bsph-config.json");
    }
}

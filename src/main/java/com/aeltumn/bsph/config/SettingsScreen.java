package com.aeltumn.bsph.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends OptionsSubScreen {
    public SettingsScreen(Screen screen) {
        super(screen, Minecraft.getInstance().options, Component.translatable("bsph.options.screen"));
    }

    @Override
    protected void addOptions() {
        this.list.addBig(
                GlowingOptions.ACTIVATION_MODE
        );
        this.list.addSmall(
                GlowingOptions.SHOW_GLOWING,
                GlowingOptions.SHOW_INVISIBILITY
        );
    }
}

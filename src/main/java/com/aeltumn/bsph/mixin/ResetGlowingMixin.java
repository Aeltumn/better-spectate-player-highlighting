package com.aeltumn.bsph.mixin;

import com.aeltumn.bsph.GlowingState;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Resets glowing when switching worlds.
 */
@Mixin(Minecraft.class)
public class ResetGlowingMixin {

    @Inject(method = "setLevel", at = @At("HEAD"))
    public void setLevel(ClientLevel clientLevel, CallbackInfo ci) {
        GlowingState.INSTANCE.reset();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("HEAD"))
    public void clearLevel(Screen screen, boolean bl, boolean bl2, CallbackInfo ci) {
        GlowingState.INSTANCE.reset();
    }

    @Inject(method = "clearClientLevel", at = @At("HEAD"))
    public void resetLevel(Screen screen, CallbackInfo ci) {
        GlowingState.INSTANCE.reset();
    }
}

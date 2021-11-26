package com.github.reviversmc.bettersleeping.mixin;

import com.github.reviversmc.bettersleeping.events.EventHandler;
import com.github.reviversmc.bettersleeping.interfaces.ManagedPlayer;
import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends PlayerEntity implements ManagedPlayer {
    private int nightsAwake;

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, 1.0F, gameProfile);
    }

    @Inject(method = "sleep", at = @At("TAIL"))
    public void onSleep(BlockPos position, CallbackInfo callbackInfo) {
        EventHandler.onSleep(this);
    }

    @Inject(method = "wakeUp", at = @At("RETURN"))
    private void onWakeUp(boolean b1, boolean b2, CallbackInfo callbackInfo) {
        EventHandler.onWakeup(this, b1, b2);
    }

    @Override
    public int getNightsAwake() {
        return nightsAwake;
    }

    @Override
    public void setNightsAwake(int nights) {
        this.nightsAwake = nights;
    }

    @Override
    public void incrementNightsAwake(int amount) {
        this.nightsAwake += amount;
    }

}

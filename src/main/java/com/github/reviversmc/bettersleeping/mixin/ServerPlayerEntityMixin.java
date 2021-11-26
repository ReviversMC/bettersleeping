package com.github.reviversmc.bettersleeping.mixin;

import com.github.reviversmc.bettersleeping.events.EventHandler;
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
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "sleep", at = @At("TAIL"))
    public void onSleep(BlockPos position, CallbackInfo callbackInfo) {
        EventHandler.onSleep(this);
    }

    @Inject(method = "wakeUp", at = @At("RETURN"))
    private void onWakeUp(boolean bl, boolean updateSleepingPlayers, CallbackInfo callbackInfo) {
        EventHandler.onWakeup(this, updateSleepingPlayers);
    }

}

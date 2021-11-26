package com.github.reviversmc.bettersleeping.mixin;

import com.github.reviversmc.bettersleeping.events.EventHandler;
import com.github.reviversmc.bettersleeping.interfaces.SleepManaged;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends PlayerEntity implements SleepManaged {

    private int nightsAwake;

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, 1.0F ,  gameProfile);
    }

    @Inject(method = "sleep", at = @At("TAIL"))
    public void onSleep(BlockPos pos, CallbackInfo ci) {
        EventHandler.onSleep(this);
    }

    @Inject(method = "wakeUp", at = @At("RETURN"))
    private void onWakeUp(boolean b1, boolean b2, CallbackInfo info) {
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

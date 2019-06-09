package com.extracraftx.minecraft.bettersleeping.mixin;

import java.util.TreeMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.GameRules;

@Mixin(GameRules.class)
class GameRulesMixin{

    @Shadow
    private static TreeMap<String, GameRules.Key> KEYS;
    @Shadow
    private TreeMap<String, GameRules.Value> rules;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onStaticInit(CallbackInfo info){
        KEYS.put("percentRequiredToSleep", new GameRules.Key("50", GameRules.Type.NUMERICAL_VALUE));
    }
}
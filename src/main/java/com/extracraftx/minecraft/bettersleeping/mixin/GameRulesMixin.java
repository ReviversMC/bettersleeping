package com.extracraftx.minecraft.bettersleeping.mixin;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.GameRules;

@Mixin(GameRules.class)
class GameRulesMixin{

    @Shadow
    private static <T extends GameRules.Rule<T>> GameRules.RuleKey<T> register(String key, GameRules.RuleType<T> type) {
        throw new NotImplementedException("GameRules mixin failed");
    }

    static{
        register("percentRequiredToSleep",GameRules$IntRuleAccessor.invokeOf(50));
    }

}
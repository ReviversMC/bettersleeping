package com.extracraftx.minecraft.bettersleeping.mixin;

import net.minecraft.world.GameRules;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.class)
class GameRulesMixin{

    @Shadow
    private static <T extends GameRules.Rule<T>> GameRules.RuleKey<T> register(String key, GameRules.RuleType<T> type) {
        throw new NotImplementedException("GameRules mixin failed");
    }

    static{
        register("percentRequiredToSleep",GameRules$IntRuleAccessor.invokeCreate(50));
    }

}

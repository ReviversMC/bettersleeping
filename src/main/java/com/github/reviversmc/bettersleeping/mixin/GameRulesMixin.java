package com.github.reviversmc.bettersleeping.mixin;

import net.minecraft.world.GameRules;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.class)
public
interface GameRulesMixin {

   // @Invoker
   // static <T extends GameRules.Rule<T>> GameRules.Key<T> invokeRegister(String name, GameRules.Category category, GameRules.Type<T> type) {
   //     throw new NotImplementedException("GameRules mixin failed");
   // }

}

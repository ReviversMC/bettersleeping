package com.github.reviversmc.bettersleeping.mixin;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.GameRules;

@Mixin(GameRules.IntRule.class)
public interface GameRules$IntRuleAccessor{

   // @Invoker
    //static GameRules.Type<GameRules.IntRule> invokeCreate(int defaultValue) {
     //   throw new NotImplementedException("Mixin failed");
  //  }

}

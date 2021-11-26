package com.github.reviversmc.bettersleeping;

import com.github.reviversmc.bettersleeping.config.Config;
import com.github.reviversmc.bettersleeping.events.EventHandler;
import com.github.reviversmc.bettersleeping.mixin.GameRules$IntRuleAccessor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.GameRules;

//import static com.extracraftx.minecraft.bettersleeping.mixin.GameRulesMixin.invokeRegister;

public class BetterSleeping implements ModInitializer {

	//public static GameRules.Key<GameRules.IntRule> key = invokeRegister("percentRequiredToSleep", GameRules.Category.PLAYER, GameRules$IntRuleAccessor.invokeCreate(50));
	
	@Override
	public void onInitialize() {
        Config.loadConfigs();
		ServerTickEvents.END_SERVER_TICK.register(EventHandler::onTick);
	}

}

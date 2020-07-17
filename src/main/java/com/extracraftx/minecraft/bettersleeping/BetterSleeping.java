package com.extracraftx.minecraft.bettersleeping;

import com.extracraftx.minecraft.bettersleeping.config.Config;
import com.extracraftx.minecraft.bettersleeping.events.EventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.GameRules;

public class BetterSleeping implements ModInitializer {

	public static GameRules.RuleKey<GameRules.IntRule> key = new GameRules.RuleKey<>("percentRequiredToSleep");
	
	@Override
	public void onInitialize() {
        Config.loadConfigs();
		ServerTickEvents.END_SERVER_TICK.register(EventHandler::onTick);
	}

}

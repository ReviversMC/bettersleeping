package com.extracraftx.minecraft.bettersleeping;

import com.extracraftx.minecraft.bettersleeping.config.Config;
import com.extracraftx.minecraft.bettersleeping.events.EventHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.world.GameRules;

public class BetterSleeping implements ModInitializer {

	public static GameRules.RuleKey<GameRules.IntRule> key = new GameRules.RuleKey<GameRules.IntRule>("percentRequiredToSleep");
	
	@Override
	public void onInitialize() {
        Config.loadConfigs();
		ServerTickCallback.EVENT.register((server)->{
            EventHandler.onTick(server);
        });
	}

}

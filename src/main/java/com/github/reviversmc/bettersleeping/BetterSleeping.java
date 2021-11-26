package com.github.reviversmc.bettersleeping;

import com.github.reviversmc.bettersleeping.config.Config;
import com.github.reviversmc.bettersleeping.events.EventHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BetterSleeping implements ModInitializer {

	@Override
	public void onInitialize() {
        Config.loadConfigs();
		ServerTickEvents.END_SERVER_TICK.register(EventHandler::onTick);
	}

}

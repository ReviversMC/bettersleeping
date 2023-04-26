package com.github.reviversmc.bettersleeping;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import com.github.reviversmc.bettersleeping.events.EventHandler119;

public class BetterSleeping119 implements ModInitializer {
	public static EventHandler119 eventHandler;

	@Override
	public void onInitialize() {
		eventHandler = new EventHandler119();

		ServerTickEvents.END_SERVER_TICK.register(eventHandler::onTick);
	}
}

package com.github.reviversmc.bettersleeping;

import com.github.reviversmc.bettersleeping.events.EventHandler117;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BetterSleeping117 implements ModInitializer {
    public static EventHandler117 eventHandler = new EventHandler117();

    @Override
    public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register(eventHandler::onTick);
    }
}

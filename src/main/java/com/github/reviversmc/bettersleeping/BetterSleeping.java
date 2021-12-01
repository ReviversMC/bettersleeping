package com.github.reviversmc.bettersleeping;

import com.github.reviversmc.bettersleeping.config.Config;
import com.github.reviversmc.bettersleeping.events.EventHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BetterSleeping implements ModInitializer {
    public static final String NAMESPACE = "bettersleeping";
    public static final String LOGGER_NAME = "BetterSleeping";

    private static Logger getLogger() {
        return LogManager.getLogger(LOGGER_NAME);
    }

    public static void logWarn(String name) {
        getLogger().warn(name);
    }
    public static void logWarn(String name, String msg) {
        getLogger().warn(String.format("%s: %s", name, msg));
    }

    public static void logInfo(String info) {
        getLogger().info(info);
    }

	@Override
	public void onInitialize() {
        Config.loadConfigs();
		ServerTickEvents.END_SERVER_TICK.register(EventHandler::onTick);
	}

}

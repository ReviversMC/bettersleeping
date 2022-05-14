package com.github.reviversmc.bettersleeping;

import com.github.reviversmc.bettersleeping.config.BetterSleepingConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class BetterSleeping implements ModInitializer {
    public static final String NAMESPACE = "bettersleeping";
    public static final String LOGGER_NAME = "BetterSleeping";
    public static BetterSleepingConfig config;

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
        AutoConfig.register(BetterSleepingConfig.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(BetterSleepingConfig.class).getConfig();
	}

}

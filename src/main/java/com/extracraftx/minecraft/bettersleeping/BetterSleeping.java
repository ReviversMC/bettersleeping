package com.extracraftx.minecraft.bettersleeping;

import com.extracraftx.minecraft.bettersleeping.config.Config;
import com.extracraftx.minecraft.bettersleeping.events.EventHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

public class BetterSleeping implements ModInitializer {
    
	@Override
	public void onInitialize() {
        Config.loadConfigs();
		ServerTickCallback.EVENT.register((server)->{
            EventHandler.onTick(server);
        });
	}

}

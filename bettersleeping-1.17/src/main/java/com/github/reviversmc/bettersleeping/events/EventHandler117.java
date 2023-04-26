package com.github.reviversmc.bettersleeping.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.dimension.DimensionType;

import com.github.reviversmc.bettersleeping.BetterSleeping;

public class EventHandler117 extends EventHandlerBase {
	@Override
	protected void sendPlayerMessage(PlayerEntity player, String message) {
		LiteralText formattedMessage = new LiteralText(message);

		for (String format : BetterSleeping.config.messages.messageFormatting) {
			formattedMessage.formatted(Formatting.byName(format));
		}

		player.sendSystemMessage(formattedMessage, player.getUuid());
	}

	@Override
	protected ServerWorld getServerWorld(ServerPlayerEntity player) {
		return player.getServerWorld();
	}

	@Override
	protected boolean isBedWorking(DimensionType dimension) {
		return dimension.isBedWorking();
	}
}

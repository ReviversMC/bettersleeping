package com.github.reviversmc.bettersleeping.events;

import com.github.reviversmc.bettersleeping.BetterSleeping;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.dimension.DimensionType;

public class EventHandler119 extends EventHandlerBase {

    @Override
    protected void sendPlayerMessage(PlayerEntity player, String message) {
        MutableText formattedMessage = Text.literal(message);

        for (String format : BetterSleeping.config.messages.messageFormatting) {
            formattedMessage.formatted(Formatting.byName(format));
        }
        ((ServerPlayerEntity) player).sendMessage(formattedMessage);
    }

    @Override
    protected ServerWorld getServerWorld(ServerPlayerEntity player) {
        return player.getWorld();
    }

    @Override
    protected boolean isBedWorking(DimensionType dimension) {
        return dimension.bedWorks();
    }

}

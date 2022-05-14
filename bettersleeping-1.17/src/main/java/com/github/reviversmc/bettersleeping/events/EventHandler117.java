package com.github.reviversmc.bettersleeping.events;

import com.github.reviversmc.bettersleeping.BetterSleeping;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class EventHandler117 extends EventHandlerBase {

    @Override
    protected void sendPlayerMessage(PlayerEntity player, String message) {
        LiteralText formattedMessage = new LiteralText(message);

        for (String format : BetterSleeping.config.messages.messageFormatting) {
            formattedMessage.formatted(Formatting.byName(format));
        }
        player.sendSystemMessage(formattedMessage, player.getUuid());
    }

}

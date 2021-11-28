package com.github.reviversmc.bettersleeping.events;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import com.github.reviversmc.bettersleeping.config.Config;

import org.apache.commons.lang3.text.StrSubstitutor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EventHandler {

    public static void onTick(MinecraftServer server) {
        // Apply debuffs every morning to everyone who hasn't slept in a while
        for (ServerWorld world : server.getWorlds()) {
            if (world.getTimeOfDay() % 24000 == 1) {
                List<ServerPlayerEntity> players = world.getPlayers();
                for (ServerPlayerEntity player : players) {
                    int nightsAwake = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)) / 24000;
                    if (Config.INSTANCE.awakeDebuff && nightsAwake > Config.INSTANCE.nightsBeforeDebuff) {
                        applyDebuffs(player, nightsAwake);
                    }
                }
            }
        }
    }



    private static void applyDebuffs(ServerPlayerEntity player, int nightsAwake) {
        int nightsAwakeToPunish = nightsAwake - Config.INSTANCE.nightsBeforeDebuff;
        if (nightsAwakeToPunish	< 1) {
            return;
        }

        // Send debuff message
        HashMap<String, String> args = new HashMap<>();
        args.put("nights", NumberFormat.getInstance().format(nightsAwake));
        LiteralText debuffText = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.debuffMessage, args, "{", "}"));
        for (String format : Config.INSTANCE.formatting) {
            debuffText.formatted(Formatting.byName(format));
        }
        player.sendSystemMessage(debuffText, player.getUuid());

        // Apply debuffs
        int duration = Math.min(24100, 200 + 80 * (nightsAwakeToPunish * nightsAwakeToPunish));
        int amplifier = Math.min(2, (nightsAwakeToPunish * nightsAwakeToPunish) / 10);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,           duration, 0));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,         duration, amplifier));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,         duration, amplifier));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,   duration, Math.min(1, amplifier / 2)));
        if (nightsAwakeToPunish >= 3) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,    duration / 3, 0));
        }
    }



    public static void onSleep(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        // Send asleep message to every player
        sendAsleepMessage(player.getEntityWorld());
    }



    public static void onWakeup(PlayerEntity player, boolean updateSleepingPlayers) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        if (player.getEntityWorld().getTimeOfDay() % 24000 > 12010) {
            // Player has aborted the sleeping process, since it is still night
            // Resend asleep message to every player
            sendAsleepMessage(player.getEntityWorld());
            return;
        }

        // Remove debuffs
        player.removeStatusEffect(StatusEffects.NAUSEA);
        player.removeStatusEffect(StatusEffects.SLOWNESS);
        player.removeStatusEffect(StatusEffects.WEAKNESS);
        player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        player.removeStatusEffect(StatusEffects.BLINDNESS);

        // Apply buffs
        if (Config.INSTANCE.sleepRecovery) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1));
        }

        // Send good morning message
        LiteralText skipText = new LiteralText(Config.INSTANCE.nightSkippedMessage);
        for (String format : Config.INSTANCE.formatting) {
            skipText.formatted(Formatting.byName(format));
        }
        player.sendSystemMessage(skipText, player.getUuid());
    }



    private static void sendAsleepMessage(World world) {
        List<? extends PlayerEntity> players = world.getPlayers();
        int sleepingPlayerCount = (int)players.stream().filter(LivingEntity::isSleeping).count();
        if (players.size() <= 1) {
            return;
        }

        int percentageToSkipNight = world.getServer().getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        int additionallyNeeded = MathHelper.ceil((float)(players.size() * percentageToSkipNight / 100.0f)) - sleepingPlayerCount;

        HashMap<String, String> args = new HashMap<>();
        args.put("asleep",          NumberFormat.getInstance().format(sleepingPlayerCount));
        args.put("total",           NumberFormat.getInstance().format(players.size()));
        args.put("percent",         NumberFormat.getInstance().format((sleepingPlayerCount * 100) / players.size()));
        args.put("required",        NumberFormat.getInstance().format(players.size() / percentageToSkipNight * 10));
        args.put("percentRequired", NumberFormat.getInstance().format(percentageToSkipNight));

        LiteralText sleepingMessage;
        if (additionallyNeeded > 0) {
            args.put("additionallyNeeded", NumberFormat.getInstance().format(additionallyNeeded));
            sleepingMessage = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.notEnoughPlayersAsleepMessage, args, "{", "}"));
        } else {
            sleepingMessage = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.playersAsleepMessage, args, "{", "}"));
        }
        for (String format : Config.INSTANCE.formatting) {
            sleepingMessage.formatted(Formatting.byName(format));
        }
        players.forEach(player -> {
            if (!(player instanceof ServerPlayerEntity)) {
                return;
            }
            player.sendSystemMessage(sleepingMessage, player.getUuid());
            player.sendSystemMessage(new LiteralText("sleepingPlayerCount: " + sleepingPlayerCount), player.getUuid());
            player.sendSystemMessage(new LiteralText("additionallyNeeded: " + additionallyNeeded), player.getUuid());
        });
    }

}

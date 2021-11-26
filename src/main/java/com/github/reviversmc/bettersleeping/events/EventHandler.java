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
import net.minecraft.server.world.SleepManager;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;

public class EventHandler {
    private static final SleepManager sleepManager = new SleepManager();
    private static int percentageToSkipNight;


    public static void onTick(MinecraftServer server) {
        // Get required percentage of sleeping players to skip the night
        percentageToSkipNight = server.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);

        // Apply debuffs every morning to everyone who hasn't slept in a while
        for (ServerWorld world : server.getWorlds()) {
            // Accept a range of time in case the server is laggy and ticks get skipped
            if (world.getTimeOfDay() > 0 && world.getTimeOfDay() < 100) {
                List<ServerPlayerEntity> players = world.getPlayers();
                for (ServerPlayerEntity player : players) {
                    int nightsAwake = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)) / 24000;
                    if (Config.INSTANCE.awakeDebuff && nightsAwake >= Config.INSTANCE.nightsBeforeDebuff) {
                        applyDebuffs(player, nightsAwake);
                    }
                }
            }
        }
    }



    private static void applyDebuffs(ServerPlayerEntity player, int nightsAwake) {
        int nightsAwakeToPunish = nightsAwake - Config.INSTANCE.nightsBeforeDebuff + 1;
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

        // Apply debuffs
        player.sendSystemMessage(debuffText, player.getUuid());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,           nightsAwakeToPunish * 100));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,         nightsAwakeToPunish * 100, nightsAwakeToPunish / 2));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,   nightsAwakeToPunish * 100, nightsAwakeToPunish / 2));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,         nightsAwakeToPunish * 100, nightsAwakeToPunish / 2));
        if (nightsAwakeToPunish > 2) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,    nightsAwakeToPunish * 100));
        }
    }



    public static void onSleep(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        // Send asleep message to every player
        List<? extends PlayerEntity> players = player.getEntityWorld().getPlayers();
        long sleepingPlayerCount = players.stream().filter(LivingEntity::isSleeping).count();
        if (players.size() <= 1) {
            return;
        }
        sendAsleepMessage(players, sleepingPlayerCount, players.size(), percentageToSkipNight);
    }



    public static void onWakeup(PlayerEntity player, boolean updateSleepingPlayers) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        // Apply positive effect
        if (Config.INSTANCE.sleepRecovery) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0));
        }

        // Send good morning message
        LiteralText skipText = new LiteralText(Config.INSTANCE.nightSkippedMessage);
        for (String format : Config.INSTANCE.formatting) {
            skipText.formatted(Formatting.byName(format));
        }
        player.sendSystemMessage(skipText, player.getUuid());
    }



    private static void sendAsleepMessage(List<? extends PlayerEntity> players, long asleep, int total, int percentRequired) {
        HashMap<String, String> args = new HashMap<>();
        args.put("asleep",          NumberFormat.getInstance().format(asleep));
        args.put("total",           NumberFormat.getInstance().format(players.size()));
        args.put("percent",         NumberFormat.getInstance().format((asleep * 100) / players.size()));
        args.put("required",        NumberFormat.getInstance().format(sleepManager.getNightSkippingRequirement(percentRequired)));
        args.put("percentRequired", NumberFormat.getInstance().format(percentRequired));
        LiteralText sleepingMessage = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.playersAsleepMessage, args, "{", "}"));
        for (String format : Config.INSTANCE.formatting) {
            sleepingMessage.formatted(Formatting.byName(format));
        }
        players.forEach(player -> {
            player.sendSystemMessage(sleepingMessage, player.getUuid());
        });
    }

}

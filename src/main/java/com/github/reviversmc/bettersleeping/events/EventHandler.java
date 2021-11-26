package com.github.reviversmc.bettersleeping.events;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import com.github.reviversmc.bettersleeping.config.Config;
import com.github.reviversmc.bettersleeping.interfaces.ManagedPlayer;

import org.apache.commons.lang3.text.StrSubstitutor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.level.ServerWorldProperties;

public class EventHandler {
    static int percentageToSkipNight = 50;

    public static void onTick(MinecraftServer server) {
        percentageToSkipNight = server.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);

        if (percentageToSkipNight >= 100 || percentageToSkipNight <= 0) {
            return;
        }
        for (ServerWorld world : server.getWorlds()) {
            if (world.getDimension().isBedWorking() == false) {
                continue;
            }

            List<ServerPlayerEntity> players = world.getPlayers();
            int sleepingPlayerCount = 0;
            for (PlayerEntity player : players) {
                if (player.isSleeping() && player.isSleepingLongEnough()) {
                    sleepingPlayerCount++;
                }
            }
            if (sleepingPlayerCount > 0
                    && sleepingPlayerCount != players.size()
                    && (sleepingPlayerCount * 100 / players.size()) >= percentageToSkipNight) {
                skipNight(world, players);
            }
        }
    }

    private static void skipNight(ServerWorld world, List<ServerPlayerEntity> players) {
        if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            long l = world.getLevelProperties().getTimeOfDay() + 24000L;
            world.setTimeOfDay(l - l%24000);
        }
        if (world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)) {
            ((ServerWorldProperties)world.getLevelProperties()).setRainTime(0);
            ((ServerWorldProperties)world.getLevelProperties()).setRaining(false);
            ((ServerWorldProperties)world.getLevelProperties()).setThunderTime(0);
            ((ServerWorldProperties)world.getLevelProperties()).setThundering(false);
        }
        LiteralText skipText = new LiteralText(Config.INSTANCE.nightSkippedMessage);
        for (String format : Config.INSTANCE.formatting) {
            skipText.formatted(Formatting.byName(format));
        }
        players.forEach(player -> {
            ManagedPlayer managedPlayer;
            player.sendSystemMessage(skipText, player.getUuid());

            if (player.isSleeping()) {
                player.wakeUp(false, true);
                managedPlayer = (ManagedPlayer)player;
                managedPlayer.setNightsAwake(0);
            } else {
                managedPlayer = (ManagedPlayer)player;
                managedPlayer.incrementNightsAwake(1);
                int nightsAwake = managedPlayer.getNightsAwake();
                if (Config.INSTANCE.awakeDebuff && nightsAwake >= Config.INSTANCE.nightsBeforeDebuff) {
                    // MessageFormat awakeFormat = new MessageFormat(Config.INSTANCE.debuffMessage);
                    HashMap<String, String> args = new HashMap<>();
                    args.put("nights", NumberFormat.getInstance().format(nightsAwake));
                    LiteralText debuffText = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.debuffMessage, args, "{", "}"));
                    for (String format : Config.INSTANCE.formatting) {
                        debuffText.formatted(Formatting.byName(format));
                    }
                    player.sendSystemMessage(debuffText, player.getUuid());

                    int nightsAwakeOver = nightsAwake - Config.INSTANCE.nightsBeforeDebuff + 1;
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,           nightsAwakeOver * 100));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,         1000, nightsAwakeOver / 2));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,   1000, nightsAwakeOver / 2));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,         1000, nightsAwakeOver / 2));
                    if (nightsAwakeOver > 2) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 500));
                    }
                }
            }
        });
    }


    public static void onWakeup(PlayerEntity player, boolean b1, boolean b2) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        if (Config.INSTANCE.sleepRecovery && b2 == false) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0));
            return;
        }
    }


    public static void onSleep(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        List<? extends PlayerEntity> players = player.getEntityWorld().getPlayers();
        long sleepingPlayerCount = players.stream().filter(LivingEntity::isSleeping).count();
        if (players.size() <= 1) {
            return;
        }
        sendAsleepMessage(players, sleepingPlayerCount, players.size(), percentageToSkipNight);
    }


    private static void sendAsleepMessage(List<? extends PlayerEntity> players, long asleep, int total, int percentRequired) {
        // MessageFormat sleepingFormat = new MessageFormat(Config.INSTANCE.playersAsleepMessage);
        HashMap<String, String> args = new HashMap<>();
        args.put("asleep",          NumberFormat.getInstance().format(asleep));
        args.put("total",           NumberFormat.getInstance().format(players.size()));
        args.put("percent",         NumberFormat.getInstance().format((asleep * 100) / players.size()));
        args.put("required",        NumberFormat.getInstance().format(percentRequired * players.size() / 100));
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

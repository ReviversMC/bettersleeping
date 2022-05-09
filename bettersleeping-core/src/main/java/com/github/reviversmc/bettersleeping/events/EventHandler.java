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
        if (Config.INSTANCE.getApplyAwakeDebuffs() == false) {
            return;
        }
        // Apply debuffs every morning to everyone who hasn't slept in a while
        for (ServerWorld world : server.getWorlds()) {
            if (world.getTimeOfDay() % 24000 == 1) {
                List<ServerPlayerEntity> players = world.getPlayers();
                if (players.size() <= 1 && Config.INSTANCE.getApplyAwakeDebuffsWhenAloneOnServer() == false) {
                    return;
                }
                for (ServerPlayerEntity player : players) {
                    // Only apply to survival/adventure mode
                    if (player.isSpectator() || player.isCreative()) {
                        return;
                    }
                    // Only apply in dimensions you can actually sleep in
                    if (!player.getServerWorld().getDimension().isBedWorking()) {
                        return;
                    }
                    int nightsAwake = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)) / 24000;
                    applyDebuffs(player, nightsAwake);
                }
            }
        }
    }



    private static void applyDebuffs(ServerPlayerEntity player, int nightsAwake) {
        boolean appliedDebuffs = false;
        int nightsAwakeToPunish;

        // Slowness
        nightsAwakeToPunish = nightsAwake - Config.INSTANCE.getNightsBeforeSlowness();
        if (nightsAwakeToPunish	>= 1) {
            appliedDebuffs = true;
            int duration = Math.min(24100, Math.round(Config.INSTANCE.getSlownessDurationBase()
                    * nightsAwakeToPunish * Config.INSTANCE.getSlownessDurationAmplifier()));
            int amplifier = Math.min(Config.INSTANCE.getSlownessMaxLevel(), Math.max(1, Math.round(
                    nightsAwakeToPunish * Config.INSTANCE.getSlownessLevelAmplifier())));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier));
        }
        // Weakness
        nightsAwakeToPunish = nightsAwake - Config.INSTANCE.getNightsBeforeWeakness();
        if (nightsAwakeToPunish	>= 1) {
            appliedDebuffs = true;
            int duration = Math.min(24100, Math.round(Config.INSTANCE.getWeaknessDurationBase()
                    + nightsAwakeToPunish * Config.INSTANCE.getWeaknessDurationAmplifier()));
            int amplifier = Math.min(Config.INSTANCE.getWeaknessMaxLevel(), Math.max(1, Math.round(
                    nightsAwakeToPunish * Config.INSTANCE.getWeaknessLevelAmplifier())));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, amplifier));
        }
        // Nausea
        nightsAwakeToPunish = nightsAwake - Config.INSTANCE.getNightsBeforeNausea();
        if (nightsAwakeToPunish	>= 1) {
            appliedDebuffs = true;
            int duration = Math.min(24100, Math.round(Config.INSTANCE.getNauseaDurationBase()
                    + nightsAwakeToPunish * Config.INSTANCE.getNauseaDurationAmplifier()));
            int amplifier = 0;
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, duration, amplifier));
        }
        // Mining Fatigue
        nightsAwakeToPunish = nightsAwake - Config.INSTANCE.getNightsBeforeMiningFatigue();
        if (nightsAwakeToPunish	>= 1) {
            appliedDebuffs = true;
            int duration = Math.min(24100, Math.round(Config.INSTANCE.getMiningFatigueDurationBase()
                    + nightsAwakeToPunish * Config.INSTANCE.getMiningFatigueDurationAmplifier()));
            int amplifier = Math.min(Config.INSTANCE.getMiningFatigueMaxLevel(), Math.max(1, Math.round(
                    nightsAwakeToPunish * Config.INSTANCE.getMiningFatigueLevelAmplifier())));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, amplifier));
        }
        // Blindness
        nightsAwakeToPunish = nightsAwake - Config.INSTANCE.getNightsBeforeBlindness();
        if (nightsAwakeToPunish	>= 1) {
            appliedDebuffs = true;
            int duration = Math.min(24100, Math.round(Config.INSTANCE.getBlindnessDurationBase()
                    + nightsAwakeToPunish * Config.INSTANCE.getBlindnessDurationAmplifier()));
            int amplifier = 0;
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration, amplifier));
        }


        if (!appliedDebuffs) {
            return;
        }
        // Send debuff message
        HashMap<String, String> args = new HashMap<>();
        args.put("nightsAwake", NumberFormat.getInstance().format(nightsAwake));
        LiteralText debuffText = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.getDebuffMessage(), args, "{", "}"));
        for (String format : Config.INSTANCE.getMessageFormatting()) {
            debuffText.formatted(Formatting.byName(format));
        }
        player.sendSystemMessage(debuffText, player.getUuid());
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
        if (Config.INSTANCE.getApplyAwakeDebuffs()) {
            player.removeStatusEffect(StatusEffects.NAUSEA);
            player.removeStatusEffect(StatusEffects.SLOWNESS);
            player.removeStatusEffect(StatusEffects.WEAKNESS);
            player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            player.removeStatusEffect(StatusEffects.BLINDNESS);
        }

        // Apply buffs
        if (Config.INSTANCE.getApplySleepBuffs()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,
                    Config.INSTANCE.getRegenerationDuration() * 20, Config.INSTANCE.getRegenerationLevel() - 1));
        }

        // Send good morning message
        LiteralText skipText = new LiteralText(Config.INSTANCE.getNightSkippedMessage());
        for (String format : Config.INSTANCE.getMessageFormatting()) {
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

        int percentageNeededToSkipNight = world.getServer().getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        int playersNeededToSkipNight = MathHelper.ceil((players.size() * percentageNeededToSkipNight / 100.0f));
        int playersAdditionallyNeeded = playersNeededToSkipNight - sleepingPlayerCount;

        HashMap<String, String> args = new HashMap<>();
        args.put("asleepPlayers",                   NumberFormat.getInstance().format(sleepingPlayerCount));
        args.put("totalPlayers",                    NumberFormat.getInstance().format(players.size()));
        args.put("asleepPercentage",                NumberFormat.getInstance().format((sleepingPlayerCount * 100) / players.size()));
        args.put("asleepPlayersRequired",           NumberFormat.getInstance().format(playersNeededToSkipNight));
        args.put("asleepPercentageRequired",        NumberFormat.getInstance().format(percentageNeededToSkipNight));
        args.put("asleepPlayersAdditionallyNeeded", NumberFormat.getInstance().format(playersAdditionallyNeeded));

        LiteralText sleepingMessage;
        if (playersAdditionallyNeeded > 0) {
            sleepingMessage = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.getNotEnoughPlayersAsleepMessage(), args, "{", "}"));
        } else {
            sleepingMessage = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.getPlayersAsleepMessage(), args, "{", "}"));
        }
        for (String format : Config.INSTANCE.getMessageFormatting()) {
            sleepingMessage.formatted(Formatting.byName(format));
        }
        players.forEach(player -> {
            if (!(player instanceof ServerPlayerEntity)) {
                return;
            }
            player.sendSystemMessage(sleepingMessage, player.getUuid());
        });
    }

}

package com.extracraftx.minecraft.bettersleeping.events;

import com.extracraftx.minecraft.bettersleeping.BetterSleeping;
import com.extracraftx.minecraft.bettersleeping.config.Config;
import com.extracraftx.minecraft.bettersleeping.interfaces.SleepManaged;
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
import org.apache.commons.lang3.text.StrSubstitutor;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class EventHandler{
    public static void onTick(MinecraftServer server){
        int percent = server.getGameRules().getInt(BetterSleeping.key);
        if(percent >= 100 || percent <= 0)
            return;
        server.getWorlds().forEach((world)->{
            List<ServerPlayerEntity> players = world.getPlayers();
            int count = 0;
            for (PlayerEntity p : players) {
                if(p.isSleeping() && p.isSleepingLongEnough())
                    count++;
            }
            if(count != players.size() && count*100/players.size() >= percent){
                skipNight(world, players);
            }
        });
    }

    private static void skipNight(ServerWorld world, List<ServerPlayerEntity> players){
        if(world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)){
            long l = world.getLevelProperties().getTimeOfDay() + 24000L;
            world.setTimeOfDay(l - l%24000);
        }
        if(world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)){
            ((ServerWorldProperties)world.getLevelProperties()).setRainTime(0);
            ((ServerWorldProperties)world.getLevelProperties()).setRaining(false);
            ((ServerWorldProperties)world.getLevelProperties()).setThunderTime(0);
            ((ServerWorldProperties)world.getLevelProperties()).setThundering(false);
        }
        LiteralText skipText = new LiteralText(Config.INSTANCE.nightSkippedMessage);
        for(String format : Config.INSTANCE.formatting){
            skipText.formatted(Formatting.byName(format));
        }
        players.forEach(p->{
            p.sendSystemMessage(skipText, p.getUuid());
            if(p.isSleeping()){
                p.wakeUp(false, true);
                SleepManaged sm = (SleepManaged)p;
                sm.setNightsAwake(0);
            }else{
                SleepManaged sm = (SleepManaged)p;
                sm.incrementNightsAwake(1);
                int nightsAwake = sm.getNightsAwake();
                if(Config.INSTANCE.awakeDebuff && nightsAwake >= Config.INSTANCE.nightsBeforeDebuff){
                    // MessageFormat awakeFormat = new MessageFormat(Config.INSTANCE.debuffMessage);
                    HashMap<String, String> args = new HashMap<>();
                    args.put("nights", NumberFormat.getInstance().format(nightsAwake));
                    LiteralText debuffText = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.debuffMessage, args, "{", "}"));
                    for(String format : Config.INSTANCE.formatting){
                        debuffText.formatted(Formatting.byName(format));
                    }
                    p.sendSystemMessage(debuffText, p.getUuid());
                    
                    int nightsAwakeOver = nightsAwake - Config.INSTANCE.nightsBeforeDebuff+1;
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,nightsAwakeOver*100));
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,1000,nightsAwakeOver/2));
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,1000,nightsAwakeOver/2));
                    p.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,1000,nightsAwakeOver/2));
                    if(nightsAwakeOver > 2){
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,500));
                    }
                }
            }
        });
    }

    public static void onWakeup(PlayerEntity player, boolean b1, boolean b2){
        if(!(player instanceof ServerPlayerEntity))
            return;
        if(Config.INSTANCE.sleepRecovery && !b2){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0));
            return;
        }
        List<? extends PlayerEntity> players = player.getEntityWorld().getPlayers();
        long count = players.stream().filter(LivingEntity::isSleeping).count();
        if(players.size() <= 1){
            return;
        }
        ServerPlayerEntity sp = (ServerPlayerEntity)player;
        sendAsleepMessage(players, count, players.size(), sp.getServer().getGameRules().getInt(BetterSleeping.key));
    }

    public static void onSleep(PlayerEntity player){
        if(!(player instanceof ServerPlayerEntity))
            return;
        List<? extends PlayerEntity> players = player.getEntityWorld().getPlayers();
        long count = players.stream().filter(LivingEntity::isSleeping).count();
        if(players.size() <= 1){
            return;
        }
        ServerPlayerEntity sp = (ServerPlayerEntity)player;
        sendAsleepMessage(players, count, players.size(), sp.getServer().getGameRules().getInt(BetterSleeping.key));
    }

    private static void sendAsleepMessage(List<? extends PlayerEntity> players, long asleep, int total, int percentRequired){
        // MessageFormat sleepingFormat = new MessageFormat(Config.INSTANCE.playersAsleepMessage);
        HashMap<String, String> args = new HashMap<>();
        args.put("asleep", NumberFormat.getInstance().format(asleep));
        args.put("total", NumberFormat.getInstance().format(players.size()));
        args.put("percent", NumberFormat.getInstance().format((asleep*100)/players.size()));
        args.put("required", NumberFormat.getInstance().format(percentRequired*players.size()/100));
        args.put("percentRequired", NumberFormat.getInstance().format(percentRequired));
        LiteralText sleepingMessage = new LiteralText(StrSubstitutor.replace(Config.INSTANCE.playersAsleepMessage, args, "{", "}"));
        for(String format : Config.INSTANCE.formatting){
            sleepingMessage.formatted(Formatting.byName(format));
        }
        players.forEach(p->{
            p.sendSystemMessage(sleepingMessage, p.getUuid());
        });
    }
}

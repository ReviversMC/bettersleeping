package com.extracraftx.minecraft.bettersleeping.events;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import com.extracraftx.minecraft.bettersleeping.config.Config;
import com.extracraftx.minecraft.bettersleeping.interfaces.SleepManaged;

import org.apache.commons.lang3.text.StrSubstitutor;

import net.minecraft.ChatFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class EventHandler{
    public static void onTick(MinecraftServer server){
        int percent = server.getGameRules().getInteger("percentRequiredToSleep");
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
        if(world.getGameRules().getBoolean("doDaylightCycle")){
            long l = world.getLevelProperties().getTimeOfDay() + 24000L;
            world.setTimeOfDay(l - l%24000);
        }
        if(world.getGameRules().getBoolean("doWeatherCycle")){
            world.getLevelProperties().setRainTime(0);
            world.getLevelProperties().setRaining(false);
            world.getLevelProperties().setThunderTime(0);
            world.getLevelProperties().setThundering(false);
        }
        TextComponent skipText = new TextComponent(Config.INSTANCE.nightSkippedMessage);
        for(String format : Config.INSTANCE.formatting){
            skipText.applyFormat(ChatFormat.getFormatByName(format));
        }
        players.forEach(p->{
            p.sendMessage(skipText);
            if(p.isSleeping()){
                p.wakeUp(false, false, true);
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
                    TextComponent debuffText = new TextComponent(StrSubstitutor.replace(Config.INSTANCE.debuffMessage, args, "{", "}"));
                    for(String format : Config.INSTANCE.formatting){
                        debuffText.applyFormat(ChatFormat.getFormatByName(format));
                    }
                    p.sendMessage(debuffText);
                    
                    int nightsAwakeOver = nightsAwake - Config.INSTANCE.nightsBeforeDebuff+1;
                    p.addPotionEffect(new StatusEffectInstance(StatusEffects.NAUSEA,nightsAwakeOver*100));
                    p.addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,1000,nightsAwakeOver/2));
                    p.addPotionEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,1000,nightsAwakeOver/2));
                    p.addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,1000,nightsAwakeOver/2));
                    if(nightsAwakeOver > 2){
                        p.addPotionEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,500));
                    }
                }
            }
        });
    }

    public static void onWakeup(PlayerEntity player, boolean b1, boolean b2, boolean b3){
        if(Config.INSTANCE.sleepRecovery && !b2){
            player.addPotionEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0));
            return;
        }
        List<? extends PlayerEntity> players = player.getEntityWorld().getPlayers();
        long count = players.stream().filter(LivingEntity::isSleeping).count();
        if(players.size() <= 1){
            return;
        }
        sendAsleepMessage(players, count, players.size());
    }

    public static void onSleep(PlayerEntity player){
        List<? extends PlayerEntity> players = player.getEntityWorld().getPlayers();
        long count = players.stream().filter(LivingEntity::isSleeping).count();
        if(players.size() <= 1){
            return;
        }
        sendAsleepMessage(players, count, players.size());
    }

    private static void sendAsleepMessage(List<? extends PlayerEntity> players, long asleep, int total){
        // MessageFormat sleepingFormat = new MessageFormat(Config.INSTANCE.playersAsleepMessage);
        HashMap<String, String> args = new HashMap<>();
        args.put("asleep", NumberFormat.getInstance().format(asleep));
        args.put("total", NumberFormat.getInstance().format(players.size()));
        args.put("percent", NumberFormat.getInstance().format((asleep*100)/players.size()));
        TextComponent sleepingMessage = new TextComponent(StrSubstitutor.replace(Config.INSTANCE.playersAsleepMessage, args, "{", "}"));
        for(String format : Config.INSTANCE.formatting){
            sleepingMessage.applyFormat(ChatFormat.getFormatByName(format));
        }
        players.forEach(p->{
            p.sendMessage(sleepingMessage);
        });
    }
}
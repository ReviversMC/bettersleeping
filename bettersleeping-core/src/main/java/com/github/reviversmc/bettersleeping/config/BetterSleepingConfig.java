package com.github.reviversmc.bettersleeping.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.reviversmc.bettersleeping.BetterSleeping;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = BetterSleeping.NAMESPACE)
public class BetterSleepingConfig implements ConfigData {

    @ConfigEntry.Category("messages")
    @ConfigEntry.Gui.TransitiveObject
    public Messages messages = new Messages();

    public static class Messages {
        public String nightSkippedMessage = "Rise and shine!";

        @Comment(
            "Placeholders:\n" +
            "§f- {nightsAwake}\n" +
            "§7   Number of nights skipped"
        )
        public String debuffMessage = "You have been awake for {nightsAwake} nights and have been given a debuff.";

        @Comment(
            "Placeholders:\n" +
            "§f- {asleepPlayers}\n" +
            "§7   Number of asleep players\n" +
            "§f- {totalPlayers}\n" +
            "§7   Total number of players\n" +
            "§f- {asleepPercentage}\n" +
            "§7   Percentage of players that are asleep\n" +
            "§f- {asleepPlayersRequired}\n" +
            "§7   Number of players required to skip the night\n" +
            "§f- {asleepPercentageRequired}\n" +
            "§7   Percentage of players required to skip the night\n" +
            "§f- {asleepPlayersAdditionallyNeeded}\n" +
            "§7   Number of players additionally needed to sleep\n" +
            "§7   to skip the night"
        )
        public String playersAsleepMessage = "{asleepPlayers}/{totalPlayers} ({asleepPercentage}%) players are now sleeping!";

        @Comment(
            "Placeholders:\n" +
            "§f- {asleepPlayers}\n" +
            "§7   Number of asleep players\n" +
            "§f- {totalPlayers}\n" +
            "§7   Total number of players\n" +
            "§f- {asleepPercentage}\n" +
            "§7   Percentage of players that are asleep\n" +
            "§f- {asleepPlayersRequired}\n" +
            "§7   Number of players required to skip the night\n" +
            "§f- {asleepPercentageRequired}\n" +
            "§7   Percentage of players required to skip the night\n" +
            "§f- {asleepPlayersAdditionallyNeeded}\n" +
            "§7   Number of players additionally needed to sleep\n" +
            "§7   to skip the night"
        )
        public String notEnoughPlayersAsleepMessage = "{asleepPlayers}/{totalPlayers} ({asleepPercentage}%) players are now sleeping. {asleepPlayersAdditionallyNeeded} more required to skip the night!";

        @Comment("List of formatting codes. See here: https://minecraft.fandom.com/wiki/Formatting_codes")
        public List<String> messageFormatting = new ArrayList<>(Arrays.asList("gold"));
    }


    @ConfigEntry.Category("buffs")
    @ConfigEntry.Gui.TransitiveObject
    public Buffs buffs = new Buffs();

    public static class Buffs {
        public boolean applySleepBuffs = true;
        public int regenerationDuration = 15;
        public int regenerationLevel = 2;
    }


    @ConfigEntry.Category("debuffs")
    @ConfigEntry.Gui.TransitiveObject
    public Debuffs debuffs = new Debuffs();

    public static class Debuffs {
        public boolean applyAwakeDebuffs = true;
        public boolean applyAwakeDebuffsWhenAloneOnServer = true;

        @ConfigEntry.Gui.CollapsibleObject
        public LeveledDebuff slowness = new LeveledDebuff(
            2, 10, 1.3f, 1.3f, 2
        );

        @ConfigEntry.Gui.CollapsibleObject
        public LeveledDebuff weakness = new LeveledDebuff(
            3, 10, 1.3f, 1f, 1
        );

        @ConfigEntry.Gui.CollapsibleObject
        public SimpleDebuff nausea = new SimpleDebuff(
            4, 10, 1.2f
        );

        @ConfigEntry.Gui.CollapsibleObject
        public LeveledDebuff miningFatigue = new LeveledDebuff(
            4, 10, 1.2f, 1.1f, 2
        );

        @ConfigEntry.Gui.CollapsibleObject
        public SimpleDebuff blindness = new SimpleDebuff(
            5, 7, 1.2f
        );


        // See LeveledDebuff's comment.
        // This interface is needed so EventHandler#applyDebuffs'
        //   loop doesn't have to be duplicated.
        // @Deprecated
        public interface Debuff {
            int allowedAwakeNightsBefore();
            int baseDuration();
            float durationAmplifier();
        }

        public static class SimpleDebuff implements Debuff {
            @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
            public int allowedAwakeNightsBefore;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
            public int baseDuration;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 5)
            public float durationAmplifier;

            public SimpleDebuff(
                int allowedAwakeNightsBefore,
                int baseDuration,
                float durationAmplifier
            ) {
                this.allowedAwakeNightsBefore = allowedAwakeNightsBefore;
                this.baseDuration = baseDuration;
                this.durationAmplifier = durationAmplifier;
            }

            @Override
            public int allowedAwakeNightsBefore() {
                return allowedAwakeNightsBefore;
            }

            @Override
            public int baseDuration() {
                return baseDuration;
            }

            @Override
            public float durationAmplifier() {
                return durationAmplifier;
            }
        }

        // Ideally, LeveledDebuff would just extend SimpleDebuff
        //   to reduce the amount of code duplication.
        // However, Cloth Config can't handle inheritance yet
        //   so the GUI wouldn't show the extended classes' fields.
        public static class LeveledDebuff implements Debuff {
            @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
            public int allowedAwakeNightsBefore;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
            public int baseDuration;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 5)
            public float durationAmplifier;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 3)
            public float levelAmplifier;
            @ConfigEntry.BoundedDiscrete(min = 1, max = 255)
            public int maxLevel;

            public LeveledDebuff(
                int allowedAwakeNightsBefore,
                int baseDuration,
                float durationAmplifier,
                float levelAmplifier,
                int maxLevel
            ) {
                this.allowedAwakeNightsBefore = allowedAwakeNightsBefore;
                this.baseDuration = baseDuration;
                this.durationAmplifier = durationAmplifier;
                this.levelAmplifier = levelAmplifier;
                this.maxLevel = maxLevel;
            }

            @Override
            public int allowedAwakeNightsBefore() {
                return allowedAwakeNightsBefore;
            }

            @Override
            public int baseDuration() {
                return baseDuration;
            }

            @Override
            public float durationAmplifier() {
                return durationAmplifier;
            }
        }

    }
}
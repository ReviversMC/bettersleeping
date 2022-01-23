package com.github.reviversmc.bettersleeping.config;

import java.io.File;
import java.io.IOException;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.yaml.YamlWriter;
import com.github.reviversmc.bettersleeping.BetterSleeping;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    public static final Config INSTANCE = new Config();
    private final File configFile;
    private final FileConfig config;


    public Config() {
        configFile = new File(FabricLoader.getInstance().getConfigDir() + "/bettersleeping.yaml");
        boolean configJustCreated = false;
        try {
            if (configFile.createNewFile()) {
                configJustCreated = true;
            }
        } catch (IOException e) {
            BetterSleeping.logWarn("Couldn't load BetterSleeping config file! Your settings won't be saved", ExceptionUtils.getStackTrace(e));
        }
        config = FileConfig.of(configFile);
        if (configJustCreated) {
            save();
        }
    }


    // DEFAULT VALUES
    // ---------- Messages ----------
    private String nightSkippedMessage = "Rise and shine!";
    private String debuffMessage = "You have been awake for {nightsAwake} nights and have been given a debuff.";
    private String playersAsleepMessage = "{asleepPlayers}/{totalPlayers} ({asleepPercentage}%) players are now sleeping!";
    private String notEnoughPlayersAsleepMessage = "{asleepPlayers}/{totalPlayers} ({asleepPercentage}%) players are now sleeping. {asleepPlayersAdditionallyNeeded} more required to skip the night!";
    private String[] messageFormatting = new String[] {"gold"};

    // ---------- Buffs ----------
    private boolean applySleepBuffs = true;
    // Regeneration
    private int regenerationDuration = 15;
    private int regenerationLevel = 2;

    // ---------- Debuffs ----------
    private boolean applyAwakeDebuffs = true;
    private boolean applyAwakeDebuffsWhenAloneOnServer = true;
    // Slowness
    private int nightsBeforeSlowness = 2;
    private int slownessDurationBase = 10;
    private float slownessDurationAmplifier = 1.3f;
    private float slownessLevelAmplifier = 1.3f;
    private int slownessMaxLevel = 2;
    // Weakness
    private int nightsBeforeWeakness = 3;
    private int weaknessDurationBase = 10;
    private float weaknessDurationAmplifier = 1.3f;
    private float weaknessLevelAmplifier = 1f;
    private int weaknessMaxLevel = 1;
    // Nausea
    private int nightsBeforeNausea = 4;
    private int nauseaDurationBase = 10;
    private float nauseaDurationAmplifier = 1.2f;
    // Mining Fatigue
    private int nightsBeforeMiningFatigue = 4;
    private int miningFatigueDurationBase = 10;
    private float miningFatigueDurationAmplifier = 1.2f;
    private float miningFatigueLevelAmplifier = 1.1f;
    private int miningFatigueMaxLevel = 2;
    // Blindness
    private int nightsBeforeBlindness = 5;
    private int blindnessDurationBase = 7;
    private float blindnessDurationAmplifier = 1.2f;



    public void load() {
        config.load();
        // ---------- Messages ----------
        nightSkippedMessage = config.get("nightSkippedMessage");
        debuffMessage = config.get("debuffMessage");
        playersAsleepMessage = config.get("playersAsleepMessage");
        notEnoughPlayersAsleepMessage = config.get("notEnoughPlayersAsleepMessage");
        messageFormatting = config.get("messageFormatting");

        // ---------- Buffs ----------
        applySleepBuffs = config.get("applySleepBuffs");
        // Regeneration
        regenerationDuration = config.get("regenerationDuration");
        regenerationLevel = config.get("regenerationLevel");

        // ---------- Debuffs ----------
        applyAwakeDebuffs = config.get("applyAwakeDebuffs");
        applyAwakeDebuffsWhenAloneOnServer = config.get("applyAwakeDebuffsWhenAloneOnServer");
        // Slowness
        nightsBeforeSlowness = config.get("nightsBeforeSlowness");
        slownessDurationBase = config.get("slownessDurationBase");
        slownessDurationAmplifier = config.get("slownessDurationAmplifier");
        slownessLevelAmplifier = config.get("slownessLevelAmplifier");
        slownessMaxLevel = config.get("slownessMaxLevel");
        // Weakness
        nightsBeforeWeakness = config.get("nightsBeforeWeakness");
        weaknessDurationBase = config.get("weaknessDurationBase");
        weaknessDurationAmplifier = config.get("weaknessDurationAmplifier");
        weaknessLevelAmplifier = config.get("weaknessLevelAmplifier");
        weaknessMaxLevel = config.get("weaknessMaxLevel");
        // Nausea
        nightsBeforeNausea = config.get("nightsBeforeNausea");
        nauseaDurationBase = config.get("nauseaDurationBase");
        nauseaDurationAmplifier = config.get("nauseaDurationAmplifier");
        // Mining Fatigue
        nightsBeforeMiningFatigue = config.get("nightsBeforeMiningFatigue");
        miningFatigueDurationBase = config.get("miningFatigueDurationBase");
        miningFatigueDurationAmplifier = config.get("miningFatigueDurationAmplifier");
        miningFatigueLevelAmplifier = config.get("miningFatigueLevelAmplifier");
        miningFatigueMaxLevel = config.get("miningFatigueMaxLevel");
        // Blindness
        nightsBeforeBlindness = config.get("nightsBeforeBlindness");
        blindnessDurationBase = config.get("blindnessDurationBase");
        blindnessDurationAmplifier = config.get("blindnessDurationAmplifier");
    }

    private void save() {
        // ---------- Messages ----------
        config.set("nightSkippedMessage", nightSkippedMessage);
        config.set("debuffMessage", debuffMessage);
        config.set("playersAsleepMessage", playersAsleepMessage);
        config.set("notEnoughPlayersAsleepMessage", notEnoughPlayersAsleepMessage);
        config.set("messageFormatting", messageFormatting);

        // ---------- Buffs ----------
        config.set("applySleepBuffs", applySleepBuffs);
        // Regeneration
        config.set("regenerationDuration", regenerationDuration);
        config.set("regenerationLevel", regenerationLevel);

        // ---------- Debuffs ----------
        config.set("applyAwakeDebuffs", applyAwakeDebuffs);
        config.set("applyAwakeDebuffsWhenAloneOnServer", applyAwakeDebuffsWhenAloneOnServer);
        // Slowness
        config.set("nightsBeforeSlowness", nightsBeforeSlowness);
        config.set("slownessDurationBase", slownessDurationBase);
        config.set("slownessDurationAmplifier", slownessDurationAmplifier);
        config.set("slownessLevelAmplifier", slownessLevelAmplifier);
        config.set("slownessMaxLevel", slownessMaxLevel);
        // Weakness
        config.set("nightsBeforeWeakness", nightsBeforeWeakness);
        config.set("weaknessDurationBase", weaknessDurationBase);
        config.set("weaknessDurationAmplifier", weaknessDurationAmplifier);
        config.set("weaknessLevelAmplifier", weaknessLevelAmplifier);
        config.set("weaknessMaxLevel", weaknessMaxLevel);
        // Nausea
        config.set("nightsBeforeNausea", nightsBeforeNausea);
        config.set("nauseaDurationBase", nauseaDurationBase);
        config.set("nauseaDurationAmplifier", nauseaDurationAmplifier);
        // Mining Fatigue
        config.set("nightsBeforeMiningFatigue", nightsBeforeMiningFatigue);
        config.set("miningFatigueDurationBase", miningFatigueDurationBase);
        config.set("miningFatigueDurationAmplifier", miningFatigueDurationAmplifier);
        config.set("miningFatigueLevelAmplifier", miningFatigueLevelAmplifier);
        config.set("miningFatigueMaxLevel", miningFatigueMaxLevel);
        // Blindness
        config.set("nightsBeforeBlindness", nightsBeforeBlindness);
        config.set("blindnessDurationBase", blindnessDurationBase);
        config.set("blindnessDurationAmplifier", blindnessDurationAmplifier);

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(FlowStyle.BLOCK);
        // dumperOptions.setDefaultScalarStyle(ScalarStyle.FOLDED);
        dumperOptions.setSplitLines(false);
        dumperOptions.setIndicatorIndent(2);
        dumperOptions.setIndentWithIndicator(true);
        YamlWriter writer = new YamlWriter(dumperOptions);
        writer.write(config, configFile, WritingMode.REPLACE);
    }



    public String getNightSkippedMessage() {
        return this.nightSkippedMessage;
    }

    public void setNightSkippedMessage(String nightSkippedMessage) {
        this.nightSkippedMessage = nightSkippedMessage;
        save();
    }

    public String getDebuffMessage() {
        return this.debuffMessage;
    }

    public void setDebuffMessage(String debuffMessage) {
        this.debuffMessage = debuffMessage;
        save();
    }

    public String getPlayersAsleepMessage() {
        return this.playersAsleepMessage;
    }

    public void setPlayersAsleepMessage(String playersAsleepMessage) {
        this.playersAsleepMessage = playersAsleepMessage;
        save();
    }

    public String getNotEnoughPlayersAsleepMessage() {
        return this.notEnoughPlayersAsleepMessage;
    }

    public void setNotEnoughPlayersAsleepMessage(String notEnoughPlayersAsleepMessage) {
        this.notEnoughPlayersAsleepMessage = notEnoughPlayersAsleepMessage;
        save();
    }

    public String[] getMessageFormatting() {
        return this.messageFormatting;
    }

    public void setMessageFormatting(String[] messageFormatting) {
        this.messageFormatting = messageFormatting;
        save();
    }

    public boolean getApplySleepBuffs() {
        return this.applySleepBuffs;
    }

    public void setApplySleepBuffs(boolean applySleepBuffs) {
        this.applySleepBuffs = applySleepBuffs;
        save();
    }

    public int getRegenerationDuration() {
        return this.regenerationDuration;
    }

    public void setRegenerationDuration(int regenerationDuration) {
        this.regenerationDuration = regenerationDuration;
        save();
    }

    public int getRegenerationLevel() {
        return this.regenerationLevel;
    }

    public void setRegenerationLevel(int regenerationLevel) {
        this.regenerationLevel = regenerationLevel;
        save();
    }

    public boolean getApplyAwakeDebuffs() {
        return this.applyAwakeDebuffs;
    }

    public void setApplyAwakeDebuffs(boolean applyAwakeDebuffs) {
        this.applyAwakeDebuffs = applyAwakeDebuffs;
        save();
    }

    public boolean getApplyAwakeDebuffsWhenAloneOnServer() {
        return this.applyAwakeDebuffsWhenAloneOnServer;
    }

    public void setApplyAwakeDebuffsWhenAloneOnServer(boolean applyAwakeDebuffsWhenAloneOnServer) {
        this.applyAwakeDebuffsWhenAloneOnServer = applyAwakeDebuffsWhenAloneOnServer;
        save();
    }

    public int getNightsBeforeSlowness() {
        return this.nightsBeforeSlowness;
    }

    public void setNightsBeforeSlowness(int nightsBeforeSlowness) {
        this.nightsBeforeSlowness = nightsBeforeSlowness;
        save();
    }

    public int getSlownessDurationBase() {
        return this.slownessDurationBase;
    }

    public void setSlownessDurationBase(int slownessDurationBase) {
        this.slownessDurationBase = slownessDurationBase;
        save();
    }

    public float getSlownessDurationAmplifier() {
        return this.slownessDurationAmplifier;
    }

    public void setSlownessDurationAmplifier(float slownessDurationAmplifier) {
        this.slownessDurationAmplifier = slownessDurationAmplifier;
        save();
    }

    public float getSlownessLevelAmplifier() {
        return this.slownessLevelAmplifier;
    }

    public void setSlownessLevelAmplifier(float slownessLevelAmplifier) {
        this.slownessLevelAmplifier = slownessLevelAmplifier;
        save();
    }

    public int getSlownessMaxLevel() {
        return this.slownessMaxLevel;
    }

    public void setSlownessMaxLevel(int slownessMaxLevel) {
        this.slownessMaxLevel = slownessMaxLevel;
        save();
    }

    public int getNightsBeforeWeakness() {
        return this.nightsBeforeWeakness;
    }

    public void setNightsBeforeWeakness(int nightsBeforeWeakness) {
        this.nightsBeforeWeakness = nightsBeforeWeakness;
        save();
    }

    public int getWeaknessDurationBase() {
        return this.weaknessDurationBase;
    }

    public void setWeaknessDurationBase(int weaknessDurationBase) {
        this.weaknessDurationBase = weaknessDurationBase;
        save();
    }

    public float getWeaknessDurationAmplifier() {
        return this.weaknessDurationAmplifier;
    }

    public void setWeaknessDurationAmplifier(float weaknessDurationAmplifier) {
        this.weaknessDurationAmplifier = weaknessDurationAmplifier;
        save();
    }

    public float getWeaknessLevelAmplifier() {
        return this.weaknessLevelAmplifier;
    }

    public void setWeaknessLevelAmplifier(float weaknessLevelAmplifier) {
        this.weaknessLevelAmplifier = weaknessLevelAmplifier;
        save();
    }

    public int getWeaknessMaxLevel() {
        return this.weaknessMaxLevel;
    }

    public void setWeaknessMaxLevel(int weaknessMaxLevel) {
        this.weaknessMaxLevel = weaknessMaxLevel;
        save();
    }

    public int getNightsBeforeNausea() {
        return this.nightsBeforeNausea;
    }

    public void setNightsBeforeNausea(int nightsBeforeNausea) {
        this.nightsBeforeNausea = nightsBeforeNausea;
        save();
    }

    public int getNauseaDurationBase() {
        return this.nauseaDurationBase;
    }

    public void setNauseaDurationBase(int nauseaDurationBase) {
        this.nauseaDurationBase = nauseaDurationBase;
        save();
    }

    public float getNauseaDurationAmplifier() {
        return this.nauseaDurationAmplifier;
    }

    public void setNauseaDurationAmplifier(float nauseaDurationAmplifier) {
        this.nauseaDurationAmplifier = nauseaDurationAmplifier;
        save();
    }

    public int getNightsBeforeMiningFatigue() {
        return this.nightsBeforeMiningFatigue;
    }

    public void setNightsBeforeMiningFatigue(int nightsBeforeMiningFatigue) {
        this.nightsBeforeMiningFatigue = nightsBeforeMiningFatigue;
        save();
    }

    public int getMiningFatigueDurationBase() {
        return this.miningFatigueDurationBase;
    }

    public void setMiningFatigueDurationBase(int miningFatigueDurationBase) {
        this.miningFatigueDurationBase = miningFatigueDurationBase;
        save();
    }

    public float getMiningFatigueDurationAmplifier() {
        return this.miningFatigueDurationAmplifier;
    }

    public void setMiningFatigueDurationAmplifier(float miningFatigueDurationAmplifier) {
        this.miningFatigueDurationAmplifier = miningFatigueDurationAmplifier;
        save();
    }

    public float getMiningFatigueLevelAmplifier() {
        return this.miningFatigueLevelAmplifier;
    }

    public void setMiningFatigueLevelAmplifier(float miningFatigueLevelAmplifier) {
        this.miningFatigueLevelAmplifier = miningFatigueLevelAmplifier;
        save();
    }

    public int getMiningFatigueMaxLevel() {
        return this.miningFatigueMaxLevel;
    }

    public void setMiningFatigueMaxLevel(int miningFatigueMaxLevel) {
        this.miningFatigueMaxLevel = miningFatigueMaxLevel;
        save();
    }

    public int getNightsBeforeBlindness() {
        return this.nightsBeforeBlindness;
    }

    public void setNightsBeforeBlindness(int nightsBeforeBlindness) {
        this.nightsBeforeBlindness = nightsBeforeBlindness;
        save();
    }

    public int getBlindnessDurationBase() {
        return this.blindnessDurationBase;
    }

    public void setBlindnessDurationBase(int blindnessDurationBase) {
        this.blindnessDurationBase = blindnessDurationBase;
        save();
    }

    public float getBlindnessDurationAmplifier() {
        return this.blindnessDurationAmplifier;
    }

    public void setBlindnessDurationAmplifier(float blindnessDurationAmplifier) {
        this.blindnessDurationAmplifier = blindnessDurationAmplifier;
        save();
    }

}

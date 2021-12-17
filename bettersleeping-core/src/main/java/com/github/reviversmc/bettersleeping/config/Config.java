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


    private String nightSkippedMessage = "Rise and shine!";
    private String debuffMessage = "You have been awake for {nights} nights and have been given a debuff.";
    private String playersAsleepMessage = "{asleep}/{total} ({percent}%) players are now sleeping!";
    private String notEnoughPlayersAsleepMessage = "{asleep}/{total} ({percent}%) players are now sleeping. {additionallyNeeded} more required to skip the night!";
    private String[] formatting = new String[] {"gold"};
    private boolean awakeDebuff = true;
    private boolean applyDebuffWhenAloneOnServer = true;
    private int nightsBeforeDebuff = 2;
    private boolean sleepRecovery = true;


    public void load() {
        config.load();
        nightSkippedMessage = config.get("nightSkippedMessage");
        debuffMessage = config.get("debuffMessage");
        playersAsleepMessage = config.get("playersAsleepMessage");
        notEnoughPlayersAsleepMessage = config.get("notEnoughPlayersAsleepMessage");
        formatting = config.get("formatting");
        awakeDebuff = config.get("awakeDebuff");
        applyDebuffWhenAloneOnServer = config.get("applyDebuffWhenAloneOnServer");
        nightsBeforeDebuff = config.get("nightsBeforeDebuff");
        sleepRecovery = config.get("sleepRecovery");
    }

    private void save() {
        config.set("nightSkippedMessage", nightSkippedMessage);
        config.set("debuffMessage", debuffMessage);
        config.set("playersAsleepMessage", playersAsleepMessage);
        config.set("notEnoughPlayersAsleepMessage", notEnoughPlayersAsleepMessage);
        config.set("formatting", formatting);
        config.set("awakeDebuff", awakeDebuff);
        config.set("applyDebuffWhenAloneOnServer", applyDebuffWhenAloneOnServer);
        config.set("nightsBeforeDebuff", nightsBeforeDebuff);
        config.set("sleepRecovery", sleepRecovery);

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

    public String[] getFormatting() {
        return this.formatting;
    }

    public void setFormatting(String[] formatting) {
        this.formatting = formatting;
        save();
    }

    public boolean getAwakeDebuff() {
        return this.awakeDebuff;
    }

    public void setAwakeDebuff(boolean awakeDebuff) {
        this.awakeDebuff = awakeDebuff;
        save();
    }

    public boolean getApplyDebuffWhenAloneOnServer() {
        return this.applyDebuffWhenAloneOnServer;
    }

    public void setApplyDebuffWhenAloneOnServer(boolean applyDebuffWhenAloneOnServer) {
        this.applyDebuffWhenAloneOnServer = applyDebuffWhenAloneOnServer;
        save();
    }

    public int getNightsBeforeDebuff() {
        return this.nightsBeforeDebuff;
    }

    public void setNightsBeforeDebuff(int nightsBeforeDebuff) {
        this.nightsBeforeDebuff = nightsBeforeDebuff;
        save();
    }

    public boolean getSleepRecovery() {
        return this.sleepRecovery;
    }

    public void setSleepRecovery(boolean sleepRecovery) {
        this.sleepRecovery = sleepRecovery;
        save();
    }

}

package com.github.reviversmc.bettersleeping.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Config {
    public String nightSkippedMessage = "Rise and shine!";
    public String debuffMessage = "You have been awake for {nights} nights and have been given a debuff.";
    public String playersAsleepMessage = "{asleep}/{total} ({percent}%) players are now sleeping!";
    public String notEnoughPlayersAsleepMessage = "{asleep}/{total} ({percent}%) players are now sleeping. {additionalNeeded} more are needed to skip the night!";
    public String[] formatting = new String[] {"gold"};
    public boolean awakeDebuff = true;
    public int nightsBeforeDebuff = 2;
    public boolean sleepRecovery = true;

    public static File configDir = new File("config");
    public static File configFile = new File("config/bettersleeping_config.json");
    public static Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
    public static Config INSTANCE = new Config();

    public static void loadConfigs() {
        try {
            configDir.mkdirs();
            if (configFile.createNewFile()) {
                FileWriter fw = new FileWriter(configFile);
                fw.append(gson.toJson(INSTANCE));
                fw.close();
            } else {
                FileReader fr = new FileReader(configFile);
                INSTANCE = gson.fromJson(fr, Config.class);
                fr.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveConfigs() {
        try {
            configDir.mkdirs();
            FileWriter fw = new FileWriter(configFile);
            fw.append(gson.toJson(INSTANCE));
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

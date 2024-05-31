package com.andcool.oauth.config;

import com.andcool.oauth.Oauth;
import org.apache.logging.log4j.Level;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UserConfig {
    public static int PORT = 8089;
    public static int TTL = 5 * 60 * 1000;

    /*
    Save config to file
     */
    public static void save() {
        final File configFile = new File("plugins/mc-oauth/config.json");
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("PORT", PORT);
        jsonConfig.put("TTL", TTL);
        try {
            Files.createDirectories(configFile.toPath().getParent());
            Files.writeString(configFile.toPath(), jsonConfig.toString());
        } catch (IOException e) {
            Oauth.betterLog(Level.ERROR, e.toString());
        }
    }

    /*
    Load config from file
     */
    public static void load() {
        final File configFile = new File("plugins/mc-oauth/config.json");
        try {
            JSONObject jsonConfig = new JSONObject(Files.readString(configFile.toPath()));
            for (String key : jsonConfig.keySet()) {
                switch (key) {
                    case "PORT" -> PORT = jsonConfig.getInt(key);
                    case "TTL" -> TTL = jsonConfig.getInt(key);
                }
            }
        } catch (Exception e) {
            Oauth.betterLog(Level.WARN, e.toString());
            save();
        }
    }
}
package com.andcool.oauth.onLogin;

import com.andcool.oauth.Oauth;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Random;
import java.util.UUID;

import org.json.JSONObject;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        UUID playerId = event.getUniqueId();

        Random random = new Random();
        int code = 100000 + random.nextInt(900000);

        String kickMessage = "Your code is: " + code;
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success");
        jsonResponse.put("nickname", playerName);
        jsonResponse.put("UUID", playerId.toString().toLowerCase());
        Oauth.expiringMap.put(String.valueOf(code), jsonResponse);
    }
}
package com.andcool.oauth;

import com.andcool.oauth.onLogin.PlayerPreLoginListener;
import org.bukkit.plugin.java.JavaPlugin;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.json.JSONObject;

import com.andcool.oauth.hashMap.ExpiringHashMap;
import com.andcool.oauth.config.UserConfig;

public final class Oauth extends JavaPlugin {
    private HttpServer server;
    public static ExpiringHashMap<String, JSONObject> expiringMap = new ExpiringHashMap<>(5 * 60 * 1000);
    public static final Logger LOGGER = LogManager.getLogger("mc-oauth");
    public static void betterLog(Level level, String message) {
        LOGGER.log(level, String.format("[%s]: %s", "mc-oauth", message));
    }

    @Override
    public void onEnable() {
        UserConfig.load();  // Load config
        expiringMap.expirationTimeMillis = UserConfig.TTL;  // set expiration time from config
        getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(), this);  // prelogin event

        try {
            server = HttpServer.create(new InetSocketAddress(UserConfig.PORT), 0);
            server.createContext("/code", new CodeHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            betterLog(Level.ERROR, e.toString());
        }
    }

    @Override
    public void onDisable() {
        if (server != null) {
            server.stop(0);
        }
    }

    static class CodeHandler implements HttpHandler {
        private static final Pattern pattern = Pattern.compile("/code/(\\w+)");

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            Matcher matcher = pattern.matcher(path);

            int status_code = 200;
            String response;

            if (matcher.matches()) {
                String code = matcher.group(1);
                JSONObject result = expiringMap.get(code);
                if (result == null){
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Code not found");
                    jsonResponse.put("status_code", 404);
                    response = jsonResponse.toString();
                    status_code = 404;
                }else{
                    response = result.toString();
                    expiringMap.remove(code);
                }
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Not found");
                jsonResponse.put("status_code", 404);
                response = jsonResponse.toString();
                status_code = 404;
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(status_code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

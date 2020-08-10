package com.hawolt;

import com.hawolt.pattern.impl.ObservableAdapter;
import com.hawolt.pattern.observer.CommandObserver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.pmw.tinylog.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class Instance {

    public static final long STARTED_AT = System.currentTimeMillis();
    public static long RESTARTED_AT;

    private static JDA manager;
    private static ObservableAdapter adapter = new ObservableAdapter();
    private static Config config;

    public Instance(String[] args) {
        if (args.length == 0) {
            Logger.error("Missing parameter for configuration file at index [0]");
        } else {
            File file = ClasspathUtil.getFile(args[0]);
            if (file.exists()) {
                try {
                    reload(Config.load(file));
                } catch (IOException | LoginException e) {
                    Logger.error("Failed to initialize startup");
                    Logger.error(e);
                }
            } else {
                Logger.error("Unable to find file {}", args[0]);
            }
        }
    }

    public ObservableAdapter getAdapter() {
        return adapter;
    }

    public static JDA getManager() {
        return manager;
    }

    public static Config getConfig() {
        return config;
    }

    public static void reload(final Config config) throws LoginException {
        Instance.config = config;
        if (manager != null) manager.shutdown();
        CommandObserver.BOT_ID = Long.parseLong(config.getOrDefault("app.client_id", "0"));
        JDABuilder tmp = JDABuilder.createLight(config.get("app.client_token")).addEventListeners(adapter);
        if (Boolean.parseBoolean(config.getOrDefault("discord.activity", "false"))) {
            tmp.setActivity(Activity.playing(getConfig().getOrDefault("discord.status", String.format("@%s commands", getConfig().get("name")))));
        }
        if (Boolean.parseBoolean(config.getOrDefault("discord.cache_policy", "false"))) {
            tmp.setMemberCachePolicy(MemberCachePolicy.ALL);
        }
        for (String intent : config.getOrDefault("discord.intents", "").split(",")) {
            tmp.enableIntents(GatewayIntent.valueOf(intent));
        }
        manager = tmp.build();
    }
}

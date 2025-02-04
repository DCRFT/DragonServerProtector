package ru.overwrite.protect.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import ru.overwrite.protect.bukkit.utils.Metrics;

import java.util.Date;

public final class ServerProtector extends ServerProtectorManager {

    @Override
    public void onEnable() {
    	long startTime = System.currentTimeMillis();
        checkPaper(logger);
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        loadConfigs(config);
        PluginManager pluginManager = server.getPluginManager();
        registerListeners(pluginManager);
        registerCommands(pluginManager, config);
        startRunners(config);
        setupLogger(config);
        logEnableDisable(message.getString("log-format.enabled"), new Date(startTime));
        if (config.getBoolean("main-settings.enable-metrics")) {
            new Metrics(this, 13347);
        }
        checkForUpdates(config, logger);
        long endTime = System.currentTimeMillis();
        logger.info("Plugin started in " + (endTime - startTime) + " ms");
    }

    @Override
    public void onDisable() {
        logEnableDisable(message.getString("log-format.disabled"), new Date());
        if (getPluginConfig().message_settings_enable_broadcasts) {
        	for (Player p : server.getOnlinePlayers()) {
        		if (p.hasPermission("serverprotector.admin")) {
            		p.sendMessage(getPluginConfig().broadcasts_disabled);
            	}
        	}
        }
        if (getConfig().getBoolean("secure-settings.shutdown-on-disable")) {
        	server.shutdown();
        }
    }
}
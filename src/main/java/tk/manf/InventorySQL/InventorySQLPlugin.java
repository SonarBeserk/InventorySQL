/**
 * Copyright (c) 2013 Exo-Network
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 *
 *    2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 *
 *    3. This notice may not be removed or altered from any source
 *    distribution.
 *
 * manf                   info@manf.tk
 */

package tk.manf.InventorySQL;

import lombok.Getter;
import net.gravitydevelopment.updater.Updater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import tk.manf.InventorySQL.api.InventorySQLAPI;
import tk.manf.InventorySQL.enums.DeveloperMessages;
import tk.manf.InventorySQL.listeners.PlayerListener;
import tk.manf.InventorySQL.manager.*;
import tk.manf.InventorySQL.tasks.SyncingTask;

import java.io.IOException;

public final class InventorySQLPlugin extends JavaPlugin {
    private static final int CURSE_PROJECT_ID = 35989;

    @Getter
    private static InventorySQLPlugin instance = null;

    @Getter
    private static PluginManager pluginManager = null;
    private CommandManager manager = null;

    @Getter
    private static SyncingTask syncingTask = null;

    @Override
    public void onEnable() {
        instance = this;
        pluginManager = getServer().getPluginManager();

        try {
            saveDefaultConfig();
            FileConfiguration debug = ConfigManager.getConfig(this, "debug.yml");
            LoggingManager.getInstance().setLevel(debug.getInt("debug-level", 1000));
            LoggingManager.getInstance().setPrefix(getDescription().getPrefix());
            ConfigManager.getInstance().initialise(this);
            if(!getConfig().getString("database.enabled").equalsIgnoreCase("true")) {
                getLogger().info("You need to configure then enable the database for this plugin to work!");
                pluginManager.disablePlugin(this);
            }

            if(!pluginManager.isPluginEnabled(this)) {return;}

            DependenciesManager.getInstance().initialise(this, getClassLoader());
            DatabaseManager.getInstance().initialise(this);

            if(!DataHandlingManager.getInstance().initialise(getClassLoader())) {
                LoggingManager.getInstance().logDeveloperMessage("manf", DeveloperMessages.HANDLING_BROKEN);
                getPluginLoader().disablePlugin(this);
            }

            if(!pluginManager.isPluginEnabled(this)) {return;}

            pluginManager.registerEvents(new PlayerListener(), this);

            InventoryLockingSystem.getInstance().initialise(this);
            manager = new CommandManager();
            manager.initialise(this, getClassLoader());
            InventorySQLAPI.getAPI().init(this);
            if (ConfigManager.getInstance().getSaveInterval() > 0) {
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                    public void run() {
                        LoggingManager.getInstance().log(LoggingManager.Level.DEBUG, "Saving Players...");
                        for (Player player : getServer().getOnlinePlayers()) {
                            LoggingManager.getInstance().log(LoggingManager.Level.DEBUG, "Saving " + player.getName());
                            DatabaseManager.getInstance().savePlayer(player);
                        }
                    }
                }, ConfigManager.getInstance().getSaveInterval(), ConfigManager.getInstance().getSaveInterval());
            }
        } catch (Exception ex) {
            LoggingManager.getInstance().log(ex);
            getPluginLoader().disablePlugin(this);
        }

        if(!pluginManager.isPluginEnabled(this)) {return;}

        //May add just a check and let the user update manually?
        if (ConfigManager.getInstance().isAutoUpdateEnabled()) {
            Updater updater = new Updater(this, CURSE_PROJECT_ID, this.getFile(), Updater.UpdateType.DEFAULT, false);
            switch (updater.getResult()) {
                case SUCCESS:
                    LoggingManager.getInstance().log(999, "Updated to Version: " + updater.getLatestFileLink());
                    break;
                case NO_UPDATE:
                    LoggingManager.getInstance().log(999, "You are up to date!");
                    break;
                default:
                    break;
            }
        }

        if (ConfigManager.getInstance().isMetricsEnabled()) {
            try {
                Metrics metrics = new Metrics(this);
                //Add Graph here
                if (metrics.start()) {
                    LoggingManager.getInstance().logDeveloperMessage("manf", DeveloperMessages.METRICS_LOADED);
                } else {
                    LoggingManager.getInstance().logDeveloperMessage("manf", DeveloperMessages.METRICS_OFF);
                }
            } catch (IOException e) {
            }
        }

        syncingTask = new SyncingTask();
        syncingTask.runTaskTimer(this, 0, 10);
    }

    @Override
    public void onDisable() {
        InventorySQLAPI.getAPI().disable(this);
        AddonManager.getInstance().disable(this);
    }
}

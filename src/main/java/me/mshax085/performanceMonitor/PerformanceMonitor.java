/*
    * PerformanceMonitor Bukkit Plugin, monitoring of server performance.
    * Copyright (C) 2012 Richard Dahlgren
    * 
    * This program is free software; you can redistribute it and/or modify
    * it under the terms of the GNU General Public License as published by
    * the Free Software Foundation; either version 3 of the License, or
    * (at your option) any later version.
    * 
    * This program is distributed in the hope that it will be useful,
    * but WITHOUT ANY WARRANTY; without even the implied warranty of
    * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    * GNU General Public License for more details.
    * 
    * You should have received a copy of the GNU General Public License
    * along with this program; if not, write to the Free Software Foundation,
    * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package me.mshax085.performanceMonitor;

import me.mshax085.performanceMonitor.listeners.CommandListener;
import me.mshax085.performanceMonitor.listeners.EventListener;
import me.mshax085.performanceMonitor.monitors.DiskMonitor;
import me.mshax085.performanceMonitor.monitors.MemoryMonitor;
import me.mshax085.performanceMonitor.monitors.RestartMonitor;
import me.mshax085.performanceMonitor.monitors.TpsMonitor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/*
 * PerformanceMonitor Class
 * 
 * @package     me.mshax085.performanceMonitor
 * @category    StartUp - Organizer
 * @author      Richard Dahlgren (MsHax085)
 */
public class PerformanceMonitor extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private final Configuration configClass = new Configuration();
    public int uniqueLogins = 0;
    private EventListener loginListener;
    private RestartMonitor restartCounter;
    private DiskMonitor diskFileSize;
    private StatBroadcast broadcast;
    private MemoryMonitor memoryMeter;
    private TpsMonitor tpsMeter;
    private FileConfiguration config = null;
    private File configFile = null;
    private boolean latestVersion = true;

    // -------------------------------------------------------------------------

    /*
     * GetBroadcaster
     * 
     * Return Broadcaster Class Refference
     * 
     * @access  public
     * @return  Broadcaster
     */
    public final StatBroadcast getBroadcaster() {
        if (this.broadcast == null) {
            this.broadcast = new StatBroadcast(this);
        }
        return this.broadcast;
    }

    /*
     * GetConfig
     * 
     * Return FileConfiguration Refference
     * 
     * @access  public
     * @return  FileConfiguration
     */
    @Override
    public final FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfigFile();
        }
        return this.config;
    }

    /*
     * GetConfigurationClass
     * 
     * Return Configuration Class Refference
     * 
     * @access  public
     * @return  Configuration
     */
    public final Configuration getConfigurationClass() {
        return this.configClass;
    }

    /*
     * GetDiskFileSize
     * 
     * Return DiskMonitor Class Refference
     * 
     * @access  public
     * @return  DiskMonitor
     */
    public final DiskMonitor getDiskFileSize() {
        if (this.diskFileSize == null) {
            this.diskFileSize = new DiskMonitor(this);
        }
        return this.diskFileSize;
    }

    /*
     * GetMemoryMeter
     * 
     * Return MemoryMonitor Class Refference
     * 
     * @access  public
     * @return  MemoryMonitor
     */
    public final MemoryMonitor getMemoryMeter() {
        if (this.memoryMeter == null) {
            this.memoryMeter = new MemoryMonitor();
        }
        return this.memoryMeter;
    }

    /*
     * GetRestartCounter
     * 
     * Return RestartMonitor Class Refference
     * 
     * @access  public
     * @return  RestartMonitor
     */
    public final RestartMonitor getRestartCounter() {
        return this.restartCounter;
    }

    /*
     * GetTpsMeter
     * 
     * Return TpsMonitor Class Refference
     * 
     * @access  public
     * @return  TpsMonitor
     */
    public final TpsMonitor getTpsMeter() {
        return this.tpsMeter;
    }

    /*
     * GetVersion
     * 
     * Return Plugin Version
     * 
     * @access  public
     * @return  String
     */
    public final String getVersion() {
        return this.getDescription().getVersion();
    }

    /*
     * IsLatestVersion
     * 
     * Returns true if plugin is of latest version
     * 
     * @access  public
     * @return  boolean
     */
    public final boolean isLatestVersion() {
        return this.latestVersion;
    }

    /**
     * Log msg as info
     *
     * @param msg the message to log
     */
    private void logMsg(String msg) {
        logMsg(msg, false);
    }

    /*
     * LogMsg
     * 
     * Send Message To Console
     * 
     * @access  private
     * @param   String
     */
    private void logMsg(String msg, boolean warning) {
        String logMsg = "[PerformanceMonitor " + getVersion() + "] " + msg;
        if (warning) {
            log.warning(logMsg);
        } else {
            log.info(logMsg);
        }
    }

    /*
     * OnEnable
     * 
     * Called on plugin start
     * 
     * @access  public
     */
    @Override
    public final void onEnable() {
        this.getConfigurationClass().update(this);
        this.validateListeners();

        final CommandListener cl = new CommandListener(this);
        this.getCommand("ss").setExecutor(cl);
        this.getCommand("serverstate").setExecutor(cl);

        this.logMsg("Plugin enabled!");
    }

    /*
     * OnDisable
     * 
     * Called on plugin disable
     * 
     * @access  public
     */
    @Override
    public final void onDisable() {
        if (this.getConfigurationClass().showTps) {
            this.registerSchedulingTasks(true);
        }
        this.logMsg("Plugin disabled!");
    }

    /*
     * RegisterSchedulingTasks
     * 
     * Start or cancel scheduling tasks
     * 
     * @access  private
     * @param   boolean
     */
    private void registerSchedulingTasks(final boolean cancel) {
        if (!cancel) {
            this.logMsg("Starting tps meter ...");
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.tpsMeter, 0L, 40L);
        } else {
            this.getServer().getScheduler().cancelTasks(this);
        }
    }

    /*
     * ReloadConfigFile
     * 
     * Refresh Stats Based On Configuration
     * 
     * @access  public
     */
    public final void reloadConfigFile() {
        if (this.configFile == null) {
            this.configFile = new File(this.getDataFolder(), "config.yml");
        }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        final InputStream defaultConfigStream = this.getResource("config.yml");
        if (defaultConfigStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
            this.config.setDefaults(defaultConfig);
            logMsg("Loading default configuration!", false);
        }
        this.getConfigurationClass().update(this);
        this.validateListeners();
    }


    /*
     * SaveConfigFile
     * 
     * Save Stats Based on Configuration
     * 
     * @access  public
     */
    public final void saveConfigFile() {
        if (this.config != null && this.configFile != null) {
            try {
                this.getConfig().save(this.configFile);
            } catch (IOException ex) {
                this.logMsg("Could not save config to " + this.configFile + "! " + ex.getMessage(), true);
            }
        }
    }

    /*
     * ValidateListeners
     * 
     * Enable listeners if disabled and enabled in config
     * 
     * @access  private
     * @return  boolean
     */
    private boolean validateListeners() {
        if ((getConfigurationClass().showLastRestart) &&
                (this.restartCounter == null)) {
            this.restartCounter = new RestartMonitor();
            this.restartCounter.setStartTime();
        }
        if ((getConfigurationClass().showTps) &&
                (this.tpsMeter == null)) {
            this.tpsMeter = new TpsMonitor(this);
            registerSchedulingTasks(false);
        }
        if (((getConfigurationClass().statusMessageUponLogin) || (getConfigurationClass().showUniquePlayerLogins)) &&
                (this.loginListener == null)) {
            this.loginListener = new EventListener(this);
            getServer().getPluginManager().registerEvents(this.loginListener, this);
        }
        return true;
    }
}
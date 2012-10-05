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

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/*
 * Configuration Class
 * 
 * @package     me.mshax085.performanceMonitor
 * @category    Configuration
 * @author      Richard Dahlgren (MsHax085)
 */
public class Configuration {
    public boolean showServerTime;
    public boolean showLastRestart;
    public boolean showMaxMemory;
    public boolean showTotalMemory;
    public boolean showFreeMemory;
    public boolean showUsedMemory;
    public boolean showLogSize;
    public boolean showFreeDiskSize;
    public boolean showLastBackup;
    public boolean showGeneratedBackups;
    public boolean showWorldSize;
    public boolean showLoadedChunks;
    public boolean showLivingEntitiesInWorld;
    public boolean showEntitiesInWorld;
    public boolean showOnlinePlayersVsMaxSlots;
    public boolean showUniquePlayerLogins;
    public boolean showCreativePlayers;
    public boolean showSurvivalPlayers;
    public boolean showAdventurePlayers;
    public boolean showTotalOperators;
    public boolean showBukkitVersion;
    public boolean showServerIpAndPort;
    public boolean showLoadedPlugins;
    public boolean showTps;
    public boolean useAverageTps;
    public boolean warnWhenTpsIsLow;
    public boolean calculateServerHealth;
    public boolean checkForUpdatesOnStart;
    public boolean statusMessageUponLogin;
    public ChatColor titleColor;
    public ChatColor labelColor;
    public ChatColor valueColor;
    public int warnAtTpsLevel;
    
    // -------------------------------------------------------------------------

    /*
     * Update
     * 
     * Update Configuration Stats
     * 
     * @access  public
     * @param   Monitor
     */
    public final void update(final Monitor monitor) {
        final FileConfiguration config = monitor.getConfig();
        ConfigurationSection section;
        
        if (config.isConfigurationSection("time")) {
            section = config.getConfigurationSection("time");
            this.showServerTime     = section.getBoolean("showServerTime");
            this.showLastRestart    = section.getBoolean("showLastRestart");
        }
        
        if (config.isConfigurationSection("memory")) {
            section = config.getConfigurationSection("memory");
            this.showMaxMemory      = section.getBoolean("showMaxMemory");
            this.showTotalMemory    = section.getBoolean("showTotalMemory");
            this.showFreeMemory     = section.getBoolean("showFreeMemory");
            this.showUsedMemory     = section.getBoolean("showUsedMemory");
        }
        
        if (config.isConfigurationSection("disk")) {
            section = config.getConfigurationSection("disk");
            this.showLogSize        = section.getBoolean("showLogSize");
            this.showFreeDiskSize   = section.getBoolean("showFreeDiskSize");
        }

        if (config.isConfigurationSection("backup")) {
            section = config.getConfigurationSection("backup");
            this.showLastBackup         = section.getBoolean("showLastBackup");
            this.showGeneratedBackups   = section.getBoolean("showGeneratedBackups");
        }

        if (config.isConfigurationSection("world")) {
            section = config.getConfigurationSection("world");
            this.showWorldSize              = section.getBoolean("showWorldSize");
            this.showLoadedChunks           = section.getBoolean("showLoadedChunks");
            this.showLivingEntitiesInWorld  = section.getBoolean("showLivingEntitiesInWorld");
            this.showEntitiesInWorld        = section.getBoolean("showEntitiesInWorld");
        }

        if (config.isConfigurationSection("player")) {
            section = config.getConfigurationSection("player");
            this.showOnlinePlayersVsMaxSlots    = section.getBoolean("showOnlinePlayersVsMaxSlots");
            this.showUniquePlayerLogins         = section.getBoolean("showUniquePlayerLogins");
            this.showCreativePlayers            = section.getBoolean("showCreativePlayers");
            this.showSurvivalPlayers            = section.getBoolean("showSurvivalPlayers");
            this.showAdventurePlayers           = section.getBoolean("showAdventurePlayers");
            this.showTotalOperators             = section.getBoolean("showTotalOperators");
        }
        
        if (config.isConfigurationSection("server")) {
            section = config.getConfigurationSection("server");
            this.showBukkitVersion      = section.getBoolean("showBukkitVersion");
            this.showServerIpAndPort    = section.getBoolean("showServerIpAndPort");
            this.showLoadedPlugins      = section.getBoolean("showLoadedPlugins");
            this.calculateServerHealth  = section.getBoolean("calculateServerHealth");
            if (section.isConfigurationSection("tps")) {
                section = section.getConfigurationSection("tps");
                this.showTps                = section.getBoolean("showTps");
                this.useAverageTps          = section.getBoolean("useAverageTps");
                this.warnWhenTpsIsLow       = section.getBoolean("warnWhenTpsIsLow");
                this.warnAtTpsLevel         = section.getInt("warnAtTpsLevel");
            }
        }

        if (config.isConfigurationSection("additional_options")) {
            section = config.getConfigurationSection("additional_options");
            this.checkForUpdatesOnStart = section.getBoolean("checkForUpdatesOnStart");
            this.statusMessageUponLogin = section.getBoolean("statusMessageUponLogin");
            
            this.labelColor = this.getColorsFromString(section.getString("labelColor"));
            this.valueColor = this.getColorsFromString(section.getString("valueColor"));
            this.titleColor = this.getColorsFromString(section.getString("titleColor"));
        }
        
        monitor.getConfig().options().copyDefaults(true);
        monitor.saveConfigFile();
    }

    /*
     * GetColorsFromString
     * 
     * Return ChatColor Depending On String Value
     * 
     * Note: Switching with strings ain't supported in Java 1.6
     * 
     * @access  public
     * @param   String
     * @return  ChatColor
     */
    public final ChatColor getColorsFromString(final String color) {
        final String lowerCase = color.toLowerCase();
        if (lowerCase.equals("aqua")) {
            return ChatColor.AQUA;
        }
        if (lowerCase.equals("black")) {
            return ChatColor.BLACK;
        }
        if (lowerCase.equals("blue")) {
            return ChatColor.BLUE;
        }
        if (lowerCase.equals("darkaqua")) {
            return ChatColor.DARK_AQUA;
        }
        if (lowerCase.equals("darkblue")) {
            return ChatColor.DARK_BLUE;
        }
        if (lowerCase.equals("darkgray")) {
            return ChatColor.DARK_GRAY;
        }
        if (lowerCase.equals("darkgreen")) {
            return ChatColor.DARK_GREEN;
        }
        if (lowerCase.equals("darkpurple")) {
            return ChatColor.DARK_PURPLE;
        }
        if (lowerCase.equals("darkred")) {
            return ChatColor.DARK_RED;
        }
        if (lowerCase.equals("gold")) {
            return ChatColor.GOLD;
        }
        if (lowerCase.equals("gray")) {
            return ChatColor.GRAY;
        }
        if (lowerCase.equals("green")) {
            return ChatColor.GREEN;
        }
        if (lowerCase.equals("lightpurple")) {
            return ChatColor.LIGHT_PURPLE;
        }
        if (lowerCase.equals("red")) {
            return ChatColor.RED;
        }
        if (lowerCase.equals("white")) {
            return ChatColor.WHITE;
        }
        if (lowerCase.equals("yellow")) {
            return ChatColor.YELLOW;
        }
        return ChatColor.AQUA;
    }
}
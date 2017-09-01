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

import me.mshax085.performanceMonitor.monitors.MemoryMonitor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

/*
 * StatBroadcast Class
 * 
 * @package     me.mshax085.performanceMonitor.messages
 * @category    broadcasting
 * @author      Richard Dahlgren (MsHax085)
 */
public class StatBroadcast {

    private final PerformanceMonitor monitor;

    // -------------------------------------------------------------------------

    /*
     * Constructor
     * 
     */
    public StatBroadcast(final PerformanceMonitor monitor) {
        this.monitor = monitor;
    }


    /*
     * BroadcastBackupStats
     * 
     * StatBroadcast backup statistics
     * 
     * @access  private
     * @param   CommandSender
     * @param   boolean
     */
    private void broadcastBackupStats(final CommandSender cs, final boolean viewAll) {
        final Configuration config = monitor.getConfigurationClass();
        final ChatColor titleColor = config.titleColor;
        final ChatColor labelColor = config.labelColor;
        final ChatColor valueColor = config.valueColor;

        if (!(config.showLastBackup || config.showGeneratedBackups)) {
            cs.sendMessage(labelColor + "This feature has to be enabled in the config.yml file!");
            return;
        }

        final String backupString = this.monitor.getDiskFileSize().getBackupData();
        cs.sendMessage(titleColor + "- Backup Stats");
        if (backupString != null) {
            String[] backupStringSplit = backupString.split("-");
            if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.lastBackup"), viewAll, config.showLastBackup)) {
                cs.sendMessage(labelColor + "    Last backup generated on: " + valueColor + backupStringSplit[0]);
            }
            if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.generatedBackups"), viewAll, config.showGeneratedBackups)) {
                cs.sendMessage(labelColor + "    Backups generated: " + valueColor + backupStringSplit[1]);
            }
        } else {
            cs.sendMessage(labelColor + "    No data to display!");
        }
    }

    /*
     * BroadcastDiskStats
     * 
     * StatBroadcast disk statistics
     * 
     * @access  private
     * @param   CommandSender
     * @param   boolean
     */
    private void broadcastDiskStats(final CommandSender cs, final boolean viewAll) {
        Configuration config = this.monitor.getConfigurationClass();

        cs.sendMessage(config.titleColor + "- Disk Stats");
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.logSize"), viewAll, config.showLogSize)) {
            long serverLogSize = this.monitor.getDiskFileSize().getServerLogSize();
            cs.sendMessage(config.labelColor + "    Server log size: " + config.valueColor + serverLogSize + " bytes (" + Math.round((float) (serverLogSize / 1024L / 1024L)) + " MB)");
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.freeDiskSize"), viewAll, config.showFreeDiskSize)) {
            cs.sendMessage(config.labelColor + "    Free disk size: " + config.valueColor + this.monitor.getDiskFileSize().getFreeDiskSpace() + " MB");
        }
    }

    /*
     * BroadcastFullStatTable
     * 
     * StatBroadcast all stats enabled
     * 
     * @access  private
     * @param   CommandSender
     */
    private void broadcastFullStatTable(final CommandSender cs) {
        Configuration config = this.monitor.getConfigurationClass();
        if ((config.showLastRestart) || (config.showServerTime)) {
            broadcastTimeStats(cs, false);
        }
        if ((config.showTotalMemory) || (config.showFreeMemory) || (config.showMaxMemory) || (config.showUsedMemory)) {
            broadcastMemoryStats(cs, false);
        }
        if ((config.showLogSize) || (config.showFreeDiskSize)) {
            broadcastDiskStats(cs, false);
        }
        if ((config.showLastBackup) || (config.showGeneratedBackups)) {
            broadcastBackupStats(cs, false);
        }
        if ((config.showWorldSize) || (config.showLivingEntitiesInWorld) || (config.showEntitiesInWorld)) {
            broadcastWorldStats(cs, false);
        }
        if ((config.showOnlinePlayersVsMaxSlots) || (config.showUniquePlayerLogins) || (config.showTotalOperators) || (config.showCreativePlayers) || (config.showSurvivalPlayers) || (config.showAdventurePlayers)) {
            broadcastPlayerStats(cs, false);
        }
        if ((config.showTps) || (config.showLoadedPlugins) || (config.calculateServerHealth) || (config.showBukkitVersion) || (config.showServerIpAndPort)) {
            broadcastServerStats(cs, false);
        }
    }

    /*
     * BroadcastMemoryStats
     * 
     * StatBroadcast memory statistics
     * 
     * @access  private
     * @param   CommandSender
     * @param   boolean
     */
    private void broadcastMemoryStats(final CommandSender cs, final boolean viewAll) {

        Configuration config = this.monitor.getConfigurationClass();
        int max = this.monitor.getMemoryMeter().getMaxRam();
        int total = this.monitor.getMemoryMeter().getTotalRam();

        cs.sendMessage(config.titleColor + "- Memory Stats");
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.maxMemory"), viewAll, config.showMaxMemory)) {
            cs.sendMessage(config.labelColor + "    Max memory for server: " + config.valueColor + max + config.labelColor + " MB");
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.totalMemory"), viewAll, config.showTotalMemory)) {
            cs.sendMessage(config.labelColor + "    Total allocated memory: " + config.valueColor + total + config.labelColor + " MB (" + total * 100 / max + "% max)");
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.freeMemory"), viewAll, config.showFreeMemory)) {
            int free = this.monitor.getMemoryMeter().getFreeRam();
            cs.sendMessage(config.labelColor + "    Free allocated memory: " + config.valueColor + free + config.labelColor + " MB (" + free * 100 / total + "% total)");

            int freeMax = max - total + free;
            cs.sendMessage(config.labelColor + "    Free memory: " + config.valueColor + freeMax + config.labelColor + " MB (" + freeMax * 100 / max + "% max)");

        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.usedMemory"), viewAll, config.showUsedMemory)) {
            int used = this.monitor.getMemoryMeter().getUsedRam();
            cs.sendMessage(config.labelColor + "    Used memory: " + config.valueColor + used + config.labelColor + " MB (" + used * 100 / max + "% max)");
        }
    }

    /*
     * BroadcastPerformanceData
     * 
     * Handle event triggered by command
     * 
     * @access  public
     * @param   CommandSender
     * @param   String
     */
    public final void broadcastPerformanceData(CommandSender cs, String part) {
        cs.sendMessage(ChatColor.DARK_GRAY + "----------------" + ChatColor.DARK_RED + "{ Performance Monitor }" + ChatColor.DARK_GRAY + "----------------");
        if (part.equals("")) {
            broadcastFullStatTable(cs);
        } else {
            broadcastStatTablePart(cs, part);
        }
        if (this.monitor.isLatestVersion()) {
            cs.sendMessage(ChatColor.DARK_GRAY + "-----------------------" + ChatColor.DARK_RED + "{ " + this.monitor.getVersion() + " }" + ChatColor.DARK_GRAY + "-----------------------");
        } else {
            cs.sendMessage(ChatColor.DARK_GRAY + "----------------" + ChatColor.DARK_RED + "{ " + this.monitor.getVersion() + " Update Available }" + ChatColor.DARK_GRAY + "----------------");
        }
    }

    /*
     * BroadcastPlayerStats
     * 
     * StatBroadcast player statistics
     * 
     * @access  private
     * @param   CommandSender
     * @param   boolean
     */
    private void broadcastPlayerStats(CommandSender cs, boolean viewAll) {
        Configuration config = this.monitor.getConfigurationClass();
        Server server = this.monitor.getServer();
        Collection<? extends Player> onlinePlayers = server.getOnlinePlayers();

        cs.sendMessage(config.titleColor + "- Player Stats");
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.onlinePlayers"), viewAll, config.showOnlinePlayersVsMaxSlots)) {
            cs.sendMessage(config.labelColor + "    Online players: " + config.valueColor + onlinePlayers.size() + "/" + server.getMaxPlayers());
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.uniqueLogins"), viewAll, config.showUniquePlayerLogins)) {
            cs.sendMessage(config.labelColor + "    Unique logins since restart: " + config.valueColor + this.monitor.uniqueLogins);
        }
        if ((viewAll) || (config.showCreativePlayers) || (config.showSurvivalPlayers) || (config.showAdventurePlayers)) {
            int creativePlayers = 0;
            int survivalPlayers = 0;
            int adventurePlayers = 0;
            if ((onlinePlayers != null) && (onlinePlayers.size() > 0)) {
                for (Player player : onlinePlayers) {
                    GameMode gameMode = player.getGameMode();
                    if (gameMode.equals(GameMode.CREATIVE)) {
                        creativePlayers++;
                    } else if (gameMode.equals(GameMode.SURVIVAL)) {
                        survivalPlayers++;
                    } else {
                        adventurePlayers++;
                    }
                }
            }
            if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.creativePlayers"), viewAll, config.showCreativePlayers)) {
                cs.sendMessage(config.labelColor + "    Online players in Creative: " + config.valueColor + creativePlayers);
            }
            if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.survivalPlayers"), viewAll, config.showSurvivalPlayers)) {
                cs.sendMessage(config.labelColor + "    Online players in Survival: " + config.valueColor + survivalPlayers);
            }
            if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.adventurePlayers"), viewAll, config.showAdventurePlayers)) {
                cs.sendMessage(config.labelColor + "    Online players in Adventure: " + config.valueColor + adventurePlayers);
            }
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.totalOperators"), viewAll, config.showTotalOperators)) {
            cs.sendMessage(config.labelColor + "    Total Operators: " + config.valueColor + server.getOperators().size());
        }
    }

    /*
     * BroadcastStatTablePart
     * 
     * StatBroadcast parts of stat table
     * 
     * @access  private
     * @param   CommandSender
     * @param   String
     */
    private void broadcastStatTablePart(CommandSender cs, String part) {
        if (part.equals("time")) {
            broadcastTimeStats(cs, true);
            return;
        }
        if (part.equals("memory")) {
            broadcastMemoryStats(cs, true);
            return;
        }
        if (part.equals("disk")) {
            broadcastDiskStats(cs, true);
            return;
        }
        if (part.equals("backup")) {
            broadcastBackupStats(cs, true);
            return;
        }
        if (part.equals("world")) {
            broadcastWorldStats(cs, true);
            return;
        }
        if (part.equals("player")) {
            broadcastPlayerStats(cs, true);
            return;
        }
        if (part.equals("server")) {
            broadcastServerStats(cs, true);
        }
    }

    /*
     * BroadcastServerStats
     * 
     * StatBroadcast Server Statistics
     * 
     * @access  private
     * @param   CommandSender
     * @param   boolean
     */
    private void broadcastServerStats(CommandSender cs, boolean viewAll) {
        Configuration config = this.monitor.getConfigurationClass();
        Server server = this.monitor.getServer();

        cs.sendMessage(config.titleColor + "- Server Stats");
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.bukkitversion"), viewAll, config.showBukkitVersion)) {
            cs.sendMessage(config.labelColor + "    Bukkit Version: " + config.valueColor + server.getBukkitVersion());
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.ipAndPort"), viewAll, config.showServerIpAndPort)) {
            String Ip = this.monitor.getServer().getIp();
            if ((Ip == null) || (Ip.length() == 0)) {
                Ip = "127.0.0.1";
            }
            cs.sendMessage(config.labelColor + "    Server IP: " + config.valueColor + Ip + ":" + server.getPort());
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.loadedPlugins"), viewAll, config.showLoadedPlugins)) {
            cs.sendMessage(config.labelColor + "    Loaded Plugins: " + config.valueColor + this.monitor.getDiskFileSize().getPluginAmount());
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.tps"), viewAll, config.showTps)) {
            float tps = this.monitor.getTpsMeter().getAverageTps();
            if (!config.useAverageTps) {
                tps = this.monitor.getTpsMeter().getTps();
            }
            cs.sendMessage(config.labelColor + "    TPS: " + config.valueColor + tps);
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.serverHealth"), viewAll, config.calculateServerHealth)) {
            cs.sendMessage(config.labelColor + "    Server Health: " + config.valueColor + getServerStatus());
        }
    }

    /*
     * BroadcastTimeStats
     * 
     * StatBroadcast time stats
     * 
     * @access  private
     * @param   CommandSender
     * @param   boolean
     */
    private void broadcastTimeStats(CommandSender cs, boolean viewAll) {
        Configuration config = this.monitor.getConfigurationClass();

        cs.sendMessage(config.titleColor + "- Time Stats");
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.serverTime"), viewAll, config.showServerTime)) {
            cs.sendMessage(config.labelColor + "    Current server time: " + config.valueColor + DateFormat.getDateTimeInstance(2, 3).format(new Date()));
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.lastRestart"), viewAll, config.showLastRestart)) {
            cs.sendMessage(config.labelColor + "    Time since last restart: " + this.monitor.getRestartCounter().getLastRestartTimeStamp(this.monitor));
        }
    }

    /*
     * BroadcastWorldStats
     * 
     * StatBroadcast world statistics
     * 
     * @access  private
     * @param   CommandSender
     * @param    boolean
     */
    private void broadcastWorldStats(CommandSender cs, boolean viewAll) {
        Configuration config = this.monitor.getConfigurationClass();

        cs.sendMessage(config.titleColor + "- World Stats");
        if (!(cs instanceof Player)) {
            cs.sendMessage(config.labelColor + "    Not available through the console");
            return;
        }
        Player player = (Player) cs;
        World world = player.getWorld();
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.worldSize"), viewAll, config.showWorldSize)) {
            cs.sendMessage(config.labelColor + "    Current world size: " + config.valueColor + this.monitor.getDiskFileSize().getWorldSize((Player) cs) / 1024L / 1024L + " MB");
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.loadedChunks"), viewAll, config.showLoadedChunks)) {
            cs.sendMessage(config.labelColor + "    Loaded chunks in this world: " + config.valueColor + world.getLoadedChunks().length);
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.livingEntities"), viewAll, config.showLivingEntitiesInWorld)) {
            cs.sendMessage(config.labelColor + "    Living entities in this world: " + config.valueColor + world.getLivingEntities().size());
        }
        if (isAllowedPermission(config, cs.hasPermission("pmonitor.show.entities"), viewAll, config.showEntitiesInWorld)) {
            cs.sendMessage(config.labelColor + "    Entities in this world: " + config.valueColor + world.getEntities().size());
        }
    }

    /*
     * GetServerStatus
     * 
     * Calculate the server health
     * 
     * @access  private
     * @return  String
     */
    private String getServerStatus() {
        MemoryMonitor memoryMeter = this.monitor.getMemoryMeter();
        int totalMem = memoryMeter.getTotalRam();
        int usedMem = totalMem - memoryMeter.getFreeRam();

        int points = 0;
        if ((totalMem >= memoryMeter.getMaxRam() - 200) && (totalMem / 2 <= usedMem)) {
            points += 10;
            if (totalMem * 0.7D <= usedMem) {
                points += 10;
                if (totalMem * 0.9D <= usedMem) {
                    points += 10;
                }
            }
        }
        if (this.monitor.getTpsMeter().getTps() < 15.0F) {
            points += 2;
        }
        if (Math.round((float) (this.monitor.getDiskFileSize().getServerLogSize() / 1024L / 1024L)) > 50) {
            points++;
            if (Math.round((float) (this.monitor.getDiskFileSize().getServerLogSize() / 1024L / 1024L)) > 100) {
                points++;
            }
        }
        String status = ChatColor.DARK_GREEN + "Good";
        if (points >= 30) {
            status = ChatColor.DARK_RED + "Critical";
        } else if (points >= 20) {
            status = ChatColor.GOLD + "Low";
        }
        return status;
    }

    private boolean isAllowedPermission(Configuration config, boolean hasPermission, boolean viewAll, boolean enabledFeature) {
        if (config.showStatsBasedOnPermissions) {
            if (hasPermission) {
                return true;
            }
        } else if ((viewAll) || (enabledFeature)) {
            return true;
        }
        return false;
    }
}
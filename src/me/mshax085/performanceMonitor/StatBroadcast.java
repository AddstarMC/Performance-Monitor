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

import java.text.DateFormat;
import java.util.Date;
import me.mshax085.performanceMonitor.monitors.MemoryMonitor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            final String[] backupStringSplit = backupString.split("-");

            if (viewAll || config.showLastBackup) {
                cs.sendMessage(labelColor + "    Last backup generated on: " + valueColor + backupStringSplit[0]);
            }

            if (viewAll || config.showGeneratedBackups) {
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
        final Configuration config = this.monitor.getConfigurationClass();
        
        cs.sendMessage(config.titleColor + "- Disk Stats");
        if (viewAll || config.showLogSize) {
            final long serverLogSize = this.monitor.getDiskFileSize().getServerLogSize();
            cs.sendMessage(config.labelColor + "    Server log size: " + config.valueColor + serverLogSize + " bytes (" + Math.round((float)(serverLogSize / 1024L / 1024L)) + " mb)");
        }
        
        if (viewAll || config.showFreeDiskSize) {
            cs.sendMessage(config.labelColor + "    Free disk size: " + config.valueColor + this.monitor.getDiskFileSize().getFreeDiskSpace() + " mb");
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
        final Configuration config = this.monitor.getConfigurationClass();
        
        if (config.showLastRestart || config.showServerTime) {
            this.broadcastTimeStats(cs, false);
        }

        if (config.showTotalMemory || config.showFreeMemory || config.showMaxMemory || config.showUsedMemory) {
            this.broadcastMemoryStats(cs, false);
        }

        if (config.showLogSize || config.showFreeDiskSize) {
            this.broadcastDiskStats(cs, false);
        }

        if (config.showLastBackup || config.showGeneratedBackups) {
            this.broadcastBackupStats(cs, false);
        }

        if (config.showWorldSize || config.showLivingEntitiesInWorld || config.showEntitiesInWorld) {
            this.broadcastWorldStats(cs, false);
        }

        if (config.showOnlinePlayersVsMaxSlots || config.showUniquePlayerLogins || config.showTotalOperators || config.showCreativePlayers || config.showSurvivalPlayers || config.showAdventurePlayers) {
            this.broadcastPlayerStats(cs, false);
        }

        if (config.showTps || config.showLoadedPlugins || config.calculateServerHealth || config.showBukkitVersion || config.showServerIpAndPort) {
            this.broadcastServerStats(cs, false);
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
        final Configuration config = this.monitor.getConfigurationClass();
        final int max   = this.monitor.getMemoryMeter().getMaxRam();
        final int total = this.monitor.getMemoryMeter().getTotalRam();

        cs.sendMessage(config.titleColor + "- Memory Stats");
        if (viewAll || config.showMaxMemory) {
            cs.sendMessage(config.labelColor + "    Max memory for server: " + config.valueColor + max + config.labelColor + " mb");
        }

        if (viewAll || config.showTotalMemory) {
            cs.sendMessage(config.labelColor + "    Total allocated memory: " + config.valueColor + total + config.labelColor + " mb (" + total * 100 / max + "%)");
        }

        if (viewAll || config.showFreeMemory) {
            final int free = this.monitor.getMemoryMeter().getFreeRam();
            cs.sendMessage(config.labelColor + "    Free allocated memory: " + config.valueColor + free + config.labelColor + " mb (" + free * 100 / total + "%)");
        }

        if (viewAll || config.showUsedMemory) {
            final int used = this.monitor.getMemoryMeter().getUsedRam();
            cs.sendMessage(config.labelColor + "    Used allocated memory: " + config.valueColor + used + config.labelColor + " mb (" + used * 100 / total + "%)");
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
    public final void broadcastPerformanceData(final CommandSender cs, final String part) {
        cs.sendMessage(ChatColor.DARK_GRAY + "----------------" + ChatColor.DARK_RED + "{ Performance Monitor }" + ChatColor.DARK_GRAY + "----------------");
        
        if (part.equals("")) {
            this.broadcastFullStatTable(cs);
        } else {
            this.broadcastStatTablePart(cs, part);
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
    private void broadcastPlayerStats(final CommandSender cs, final boolean viewAll) {
        final Configuration config = this.monitor.getConfigurationClass();
        final Server server = this.monitor.getServer();
        final Player[] onlinePlayers = server.getOnlinePlayers();
        
        cs.sendMessage(config.titleColor + "- Player Stats");
        if (viewAll || config.showOnlinePlayersVsMaxSlots) {
            cs.sendMessage(config.labelColor + "    Online players: " + config.valueColor + onlinePlayers.length + "/" + server.getMaxPlayers());
        }
        
        if (viewAll || config.showUniquePlayerLogins) {
            cs.sendMessage(config.labelColor + "    Unique logins since restart: " + config.valueColor + this.monitor.uniqueLogins);
        }
        
        if (viewAll || config.showCreativePlayers || config.showSurvivalPlayers || config.showAdventurePlayers) {
            int creativePlayers = 0, survivalPlayers = 0, adventurePlayers = 0;
            
            if (onlinePlayers != null && onlinePlayers.length > 0) {
                GameMode gameMode;
                for (Player player : onlinePlayers) {
                    gameMode = player.getGameMode();
                    if (gameMode.equals(GameMode.CREATIVE)) {
                        creativePlayers++;
                        break;
                    }
                    
                    if (gameMode.equals(GameMode.SURVIVAL)) {
                        survivalPlayers++;
                        break;
                    }
                    adventurePlayers++;
                }
            }
            
            if (viewAll || config.showCreativePlayers) {
                cs.sendMessage(config.labelColor + "    Online players in Creative: " + config.valueColor + creativePlayers);
            }
            
            if (viewAll || config.showSurvivalPlayers) {
                cs.sendMessage(config.labelColor + "    Online players in Survival: " + config.valueColor + survivalPlayers);
            }
            
            if (viewAll || config.showAdventurePlayers) {
                cs.sendMessage(config.labelColor + "    Online players in Adventure: " + config.valueColor + adventurePlayers);
            }
        }
        if (viewAll || config.showTotalOperators) {
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
    private void broadcastStatTablePart(final CommandSender cs, final String part) {
        if (part.equals("time")) {
            this.broadcastTimeStats(cs, true);
            return;
        }
        
        if (part.equals("memory")) {
            this.broadcastMemoryStats(cs, true);
            return;
        }
        
        if (part.equals("disk")) {
            this.broadcastDiskStats(cs, true);
            return;
        }
        
        if (part.equals("backup")) {
            this.broadcastBackupStats(cs, true);
            return;
        }
        
        if (part.equals("world")) {
            this.broadcastWorldStats(cs, true);
            return;
        }
        
        if (part.equals("player")) {
            this.broadcastPlayerStats(cs, true);
            return;
        }
        
        if (part.equals("server")) {
            this.broadcastServerStats(cs, true);
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
    private void broadcastServerStats(final CommandSender cs, final boolean viewAll) {
        final Configuration config = this.monitor.getConfigurationClass();
        final Server server = this.monitor.getServer();
        
        cs.sendMessage(config.titleColor + "- Server Stats");
        if (viewAll || config.showBukkitVersion) {
            cs.sendMessage(config.labelColor + "    Bukkit Version: " + config.valueColor + server.getBukkitVersion());
        }
        
        if (viewAll || config.showServerIpAndPort) {
            String Ip = this.monitor.getServer().getIp();
            if (Ip == null || Ip.length() == 0) {
                Ip = "127.0.0.1";
            }
            
            cs.sendMessage(config.labelColor + "    Server IP: " + config.valueColor + Ip + ":" + server.getPort());
        }
        
        if (viewAll || config.showLoadedPlugins) {
            cs.sendMessage(config.labelColor + "    Loaded Plugins: " + config.valueColor + this.monitor.getDiskFileSize().getPluginAmount());
        }
        
        if (viewAll || config.showTps) {
            float tps = this.monitor.getTpsMeter().getAverageTps();
            if (!config.useAverageTps) {
                tps = this.monitor.getTpsMeter().getTps();
            }
            
            cs.sendMessage(config.labelColor + "    TPS: " + config.valueColor + tps);
        }
        
        if (viewAll || config.calculateServerHealth) {
            cs.sendMessage(config.labelColor + "    Server Health: " + config.valueColor + this.getServerStatus());
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
    private void broadcastTimeStats(final CommandSender cs, final boolean viewAll) {
        final Configuration config = this.monitor.getConfigurationClass();
        
        cs.sendMessage(config.titleColor + "- Time Stats");
        if (viewAll || config.showServerTime) {
            cs.sendMessage(config.labelColor + "    Current server time: " + config.valueColor + DateFormat.getDateTimeInstance(2, 3).format(new Date()));
        }
        
        if (viewAll || config.showLastRestart) {
            cs.sendMessage(config.labelColor + "    Time since last restart: " + this.monitor.getRestartCounter().getLastRestartTimeStamp(monitor));
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
    private void broadcastWorldStats(final CommandSender cs, final boolean viewAll) {
        final Configuration config = this.monitor.getConfigurationClass();
        
        cs.sendMessage(config.titleColor + "- World Stats");
        if (!(cs instanceof Player)) {
            cs.sendMessage(config.labelColor + "    Not available through the console");
            return;
        }
        
        final Player player = (Player) cs;
        final World world = player.getWorld();
        
        if (viewAll || config.showWorldSize) {
            cs.sendMessage(config.labelColor + "    Current world size: " + config.valueColor + this.monitor.getDiskFileSize().getWorldSize(world.getName()) / 1024L / 1024L + " mb");
        }
        if (viewAll || config.showLoadedChunks) {
            cs.sendMessage(config.labelColor + "    Loaded chunks in this world: " + config.valueColor + world.getLoadedChunks().length);
        }
        if (viewAll || config.showLivingEntitiesInWorld) {
            cs.sendMessage(config.labelColor + "    Living entities in this world: " + config.valueColor + world.getLivingEntities().size());
        }
        if (viewAll || config.showEntitiesInWorld) {
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
        final MemoryMonitor memoryMeter = this.monitor.getMemoryMeter();
        final int totalMem = memoryMeter.getTotalRam();
        final int usedMem = totalMem - memoryMeter.getFreeRam();

        int points = 0;
        if (totalMem >= memoryMeter.getMaxRam() - 200 && totalMem / 2 <= usedMem) {
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
        
        if (Math.round((float)(monitor.getDiskFileSize().getServerLogSize() / 1024L / 1024L)) > 50) {
            points++;
            if (Math.round((float)(monitor.getDiskFileSize().getServerLogSize() / 1024L / 1024L)) > 100) {
                points++;
            }
        }
        
        String status = ChatColor.DARK_GREEN + "Good";
        if (points >= 30) {
            status = ChatColor.DARK_RED + "Critical!";
        } else if (points >= 20) {
            status = ChatColor.GOLD + "Low";
        }
        return status;
    }
}
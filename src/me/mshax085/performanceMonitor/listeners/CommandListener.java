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

package me.mshax085.performanceMonitor.listeners;

import me.mshax085.performanceMonitor.Monitor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/*
 * CommandListener Class
 * 
 * @package     me.mshax085.PerformanceMonitor.Listeners
 * @category    Event Listening
 * @author      Richard Dahlgren (MsHax085)
 */
public class CommandListener implements CommandExecutor {
    private final Monitor monitor;
    
    // -------------------------------------------------------------------------

    /*
     * Constructor
     * 
     */
    public CommandListener(final Monitor monitor) {
        this.monitor = monitor;
    }
    
    /*
     * IsSubCommand
     * 
     * Returns true if command is a sub-command
     * 
     * Note: Switching with strings ain't supported in Java 1.6
     * 
     * @access  private
     * @param   String
     * @return  boolean
     */
    private boolean isSubCommand(final String command) {
        if (command.equals("time")) {
            return true;
        }
        if (command.equals("memory")) {
            return true;
        }
        if (command.equals("disk")) {
            return true;
        }
        if (command.equals("backup")) {
            return true;
        }
        if (command.equals("world")) {
            return true;
        }
        if (command.equals("player")) {
            return true;
        }
        if (command.equals("server")) {
            return true;
        }
        return false;
    }

    /*
     * OnCommand
     * 
     * Listen For Command Events
     * 
     * @access  public
     * @param   CommandSender
     * @param   Command
     * @param   String
     * @param   String[]
     * @return  boolean
     */
    @Override
    public final boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        final String name = cmd.getName().toLowerCase();
        
        if (!(name.equals("ss") || name.equals("serverstate"))) {
            return false;
        }
        
        final ChatColor titleColor = this.monitor.getConfigurationClass().titleColor;
        final ChatColor labelColor = this.monitor.getConfigurationClass().labelColor;
        
        if (!cs.hasPermission("pmonitor.showstate")) {
            cs.sendMessage(titleColor + "[PerformanceMonitor] " + labelColor + "You are lacking permission: pmonitor.showstate!");
            return true;
        }
        
        if (args.length == 1) {
            final String category = args[0].toLowerCase();
            if (category.equals("reload")) {
                this.monitor.reloadConfigFile();
                cs.sendMessage(titleColor + "[PerformanceMonitor] " + labelColor + "Configuration reloaded!");
                return true;
            }
            if (this.isSubCommand(category)) {
                this.monitor.getBroadcaster().broadcastPerformanceData(cs, category);
                return true;
            } else {
                cs.sendMessage(titleColor + "[PerformanceMonitor] " + labelColor + "Invalid Sub-Command!");
                return true;
            }
                    
        }
        this.monitor.getBroadcaster().broadcastPerformanceData(cs, "");
        return true;
    }
}
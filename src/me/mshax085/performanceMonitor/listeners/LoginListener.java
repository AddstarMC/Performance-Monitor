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

import java.util.ArrayList;
import me.mshax085.performanceMonitor.Monitor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * LoginListener Class
 * 
 * @package     me.mshax085.performanceMonitor.Monitor
 * @category    Event Listening
 * @author      Richard Dahlgren (MsHax085)
 */
public class LoginListener implements Listener {
    private final Monitor monitor;
    private final ArrayList<String> names = new ArrayList();
    
    // -------------------------------------------------------------------------

    /*
     * Constructor
     * 
     */
    public LoginListener(final Monitor mon) {
        this.monitor = mon;
    }

    /*
     * OnPlayerJoin
     * 
     * Listen For Player Join Events
     * 
     * @access  public
     * @param   PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public final void onPlayerJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (this.monitor.getConfigurationClass().showUniquePlayerLogins && !this.names.contains(player.getName())) {
            this.names.add(player.getName());
            this.monitor.uniqueLogins += 1;
        }

        if (this.monitor.getConfigurationClass().statusMessageUponLogin && player.hasPermission("pmonitor.showstate")) {
            this.monitor.getBroadcaster().broadcastPerformanceData(player, "");
        }
    }
}
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

package me.mshax085.performanceMonitor.monitors;

import me.mshax085.performanceMonitor.PerformanceMonitor;
import org.bukkit.ChatColor;

/*
 * RestartMonitor Class
 * 
 * @package     me.mshax085.performanceMonitor.Restart
 * @category    Restart Logging
 * @author      Richard Dahlgren (MsHax085)
 */
public class RestartMonitor {
    private long startTime = 0L;
    
    // -------------------------------------------------------------------------

    /*
     * GetLastRestartTimeStamp
     * 
     * Get Last Restart TimeStamp
     * 
     * @access  public
     * @param   PerformanceMonitor
     * @return  String
     */
    public final String getLastRestartTimeStamp(final PerformanceMonitor monitor) {
        final ChatColor labelColor = monitor.getConfigurationClass().labelColor;
        final ChatColor valueColor = monitor.getConfigurationClass().valueColor;
        
        return "" + ChatColor.GOLD + getTimeSinceRestart() / 604800000L + labelColor + " W, " +
                    valueColor + getTimeSinceRestart() / 86400000L + labelColor + " D, " + valueColor +
                    getTimeSinceRestart() / 3600000L % 24L + labelColor + " H, " +
                    valueColor + getTimeSinceRestart() / 60000L % 60L + labelColor + " M, " +
                    valueColor + getTimeSinceRestart() / 1000L % 60L + labelColor + " S";
    }
    
    /*
     * GetMillis
     * 
     * Return current time millis
     * 
     * @access  private
     * @return  long
     */
    private long getMillis() {
        return System.currentTimeMillis();
    }

    /*
     * GetTimeSinceLastRestart
     * 
     * Get time passed since last restart
     * 
     * @return long
     */
    public final long getTimeSinceRestart() {
        return getMillis() - this.startTime;
    }
    
    /*
     * SetStartTime
     * 
     * Set last restart time
     */
    public final void setStartTime() {
        this.startTime = getMillis();
    }
}
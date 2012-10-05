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

/*
 * MemoryMonitor Class
 * 
 * @package     me.mshax085.performanceMonitor.Memory
 * @category    Memory Logging
 * @author      Richard Dahlgren (MsHax085)
 */
public class MemoryMonitor {
    
    /*
     * GetFreeRam
     * 
     * Return amount of free RAM in megabytes
     * 
     * @access  public
     * @return  int
     */
    public final int getFreeRam() {
        final Runtime runtime = Runtime.getRuntime();
        return Math.round((float)(runtime.freeMemory() / 1048576L));
    }

    /*
     * GetMaxRam
     * 
     * Return amount of max RAM in megabytes
     * 
     * @access  public
     * @return  int
     */
    public final int getMaxRam() {
        final Runtime runtime = Runtime.getRuntime();
        return Math.round((float)(runtime.maxMemory() / 1048576L));
    }

    /*
     * GetUsedRam
     * 
     * Return amount of used RAM in megabytes
     * 
     * @access  public
     * @return  int
     */
    public final int getUsedRam() {
        return getTotalRam() - getFreeRam();
    }

    /*
     * GetTotalRam
     * 
     * Return amount of total RAM in megabytes
     * 
     * @access  public
     * @return  int
     */
    public final int getTotalRam() {
        final Runtime runtime = Runtime.getRuntime();
        return Math.round((float)(runtime.totalMemory() / 1048576L));
    }
}
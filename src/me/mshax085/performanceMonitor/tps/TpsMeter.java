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

package me.mshax085.performanceMonitor.tps;

import java.util.LinkedList;
import me.mshax085.performanceMonitor.Monitor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
 * TpsMeter Class
 * 
 * @package     me.mshax085.performanceMonitor.Tps
 * @category    TPS Logging
 * @author      Richard Dahlgren (MsHax085)
 */
public class TpsMeter implements Runnable {
    private final Monitor monitor;
    private final LinkedList<Float> loggedTps = new LinkedList();
    private long lastCall = getMillis() - 3000L;
    private long lastWarning = 0L;
    private float tps = 20.0F;
    private int interval = 40;

    // -------------------------------------------------------------------------
    
    /*
     * Constructor
     * 
     */
    public TpsMeter(final Monitor mon) {
        this.monitor = mon;
    }

    /*
     * AddTps
     * 
     * Add TPS to log
     * 
     * @access  public
     * @param   Float
     */
    public void addTps(final Float tps) {
        if (tps != null && tps.floatValue() <= 20.0F) {
            this.loggedTps.add(tps);
        }
        if (this.loggedTps.size() > 10) {
            this.loggedTps.poll();
        }
    }

    /*
     * GetAverageTps
     * 
     * Return average TPS from log
     * 
     * @access  public
     * @return  float
     */
    public final float getAverageTps() {
        float amount = 0.0F;
        for (Float f : this.loggedTps) {
            if (f != null) {
                amount += f.floatValue();
            }
        }
        return amount / this.loggedTps.size();
    }
    
    /*
     * GetMillis
     * 
     * Return Current Time Millis
     * 
     * @access  private
     * @return  long
     */
    private long getMillis() {
        return System.currentTimeMillis();
    }

    /*
     * GetTps
     * 
     * Return Current Tps
     * 
     * @access  public
     * @return  float
     */
    public final float getTps() {
        return this.tps;
    }

    /*
     * Run
     * 
     * Runnable Tps Thread
     * 
     * @access  public
     */
    @Override
    public final void run() {
        final long currentTime = getMillis();
        long spentTime = (currentTime - this.lastCall) / 1000L;
        if (spentTime == 0L) {
            spentTime = 1L;
        }
        
        float calculatedTps = (float)(this.interval / spentTime);
        if (calculatedTps > 20.0F) {
            calculatedTps = 20.0F;
        }
        
        setTps(calculatedTps);
        addTps(Float.valueOf(calculatedTps));
        if (this.monitor.getConfigurationClass().warnWhenTpsIsLow && this.monitor.getConfigurationClass().warnAtTpsLevel >= getAverageTps() && this.lastWarning + 10000L < getMillis()) {
            final Player[] online = this.monitor.getServer().getOnlinePlayers();
            if (online != null && online.length > 0) {
                for (Player player : online) {
                    if (player.hasPermission("pmonitor.showstate")) {
                        player.sendMessage(ChatColor.RED + "Warning: The TPS has reached a low level!");
                    }
                }
            }
            this.lastWarning = getMillis();
        }
        this.lastCall = getMillis();
    }
    
    /*
     * SetTps
     * 
     * Set Current Tps
     * 
     * @access  private
     * @param   float
     */
    private void setTps(final float newTps) {
        this.tps = newTps;
    }
}
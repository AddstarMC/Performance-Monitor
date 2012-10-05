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

package me.mshax085.performanceMonitor.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.mshax085.performanceMonitor.Monitor;

/*
 * DiskFileSize
 * 
 * @package     me.mshax085.performanceMonitor.disk
 * @category    Disk and File size
 * @author      Richard Dahlgren (MsHax085)
 */
public class DiskFileSize {
    
    private final Monitor monitor;
    private final String filePath;
    
    // -------------------------------------------------------------------------
    
    /*
     * Constructor
     * 
     */
    public DiskFileSize(final Monitor monitor) {
        this.monitor = monitor;
        
        final File file = new File(this.monitor.getDataFolder(), "config.yml");
        this.filePath = file.getAbsolutePath().split("PerformanceMonitor")[0];
    }

    /*
     * GetPluginAmount
     * 
     * Returns the amount of plugins
     * 
     * @access  public
     * @return  int
     */
    public final int getPluginAmount() {
        final File file = new File(this.filePath);
        int plugins = 0;

        if (file.exists()) {
            final File[] listedFiles = file.listFiles();
            for (File currentFile : listedFiles) {
                if (currentFile.getName().contains(".jar")) {
                    plugins += 1;
                }
            }
        }
        return plugins;
    }
    
    /*
     * GetWorldSize
     * 
     * Returns the current world size
     * 
     * @access  public
     * @param   String
     * @return  long
     */
    public final long getWorldSize(final String worldName) {
        final ArrayList fileArray = new ArrayList();
        final String serverPath = this.filePath.split("plugins")[0];
        File[] filesInDir = new File(serverPath + worldName).listFiles();
        
        if (filesInDir != null && filesInDir.length > 0) {
            fileArray.addAll(Arrays.asList(filesInDir));
        }
        
        filesInDir = new File(serverPath + worldName + "/data").listFiles();
        if (filesInDir != null && filesInDir.length > 0) {
            fileArray.addAll(Arrays.asList(filesInDir));
        }
        
        filesInDir = new File(serverPath + worldName + "/players").listFiles();
        if (filesInDir != null && filesInDir.length > 0) {
            fileArray.addAll(Arrays.asList(filesInDir));
        }
        
        filesInDir = new File(serverPath + worldName + "/region").listFiles();
        if (filesInDir != null && filesInDir.length > 0) {
            fileArray.addAll(Arrays.asList(filesInDir));
        }
        
        int totalSizes = 0;
        if (fileArray.size() > 0) {
            for (int fileNo = 0; fileNo < fileArray.size(); fileNo++) {
                totalSizes += ((File) fileArray.get(fileNo)).length();
            }
        }
        return totalSizes;
    }

    /*
     * GetServerLogSize
     * 
     * Returns the server.log size
     * 
     * @access  public
     * @return  long
     */
    public final long getServerLogSize() {
        final String logPath = this.filePath.split("plugins")[0];
        final File logFile = new File(logPath + "/server.log");
        
        if (!logFile.exists()) {
            return 0L;
        }
        
        return logFile.length();
    }

    /*
     * GetFreeDiskSpace
     * 
     * Returns the free amount of disk space
     * 
     * @access  public
     * @return  long
     */
    public final long getFreeDiskSpace() {
        final String path = this.filePath.split("plugins")[0];
        final File directory = new File(path);
        return Math.round((float)(directory.getFreeSpace() / 1024L / 1024L));
    }
    
    /*
     * GetBackupData
     * 
     * Returns the amount of backups and last backup timestamp
     * 
     * @access  public
     * @return  String
     */
    public final String getBackupData() {
        final String path = this.filePath.split("plugins")[0];
        final File directory = new File(path + "/backups");
        
        if (!directory.exists()) {
            return null;
        }
        
        final File[] backups = directory.listFiles();
        if (backups == null && backups.length < 1) {
            return null;
        }
        
        String backupName;
        final ArrayList<String> fileNames = new ArrayList<String>();
        for (File backup : backups) {
            backupName = backup.getName();
            if (backupName.length() < 5 && backupName.substring(backupName.length() - 3, backupName.length()).equals("zip")) {
                fileNames.add(backupName);
            }
        }
        
        if (fileNames.isEmpty()) {
            return null;
        }
        
        Collections.sort(fileNames);
        final String[] lastBackup = fileNames.get(fileNames.size()).split("");
        if (lastBackup.length == 20) {
            final String year = lastBackup[1] + lastBackup[2] + lastBackup[3] + lastBackup[4];
            final String month = lastBackup[5] + lastBackup[6];
            final String day = lastBackup[7] + lastBackup[8];
            final String hour = lastBackup[10] + lastBackup[11];
            final String min = lastBackup[12] + lastBackup[13];
            return year + ":" + month + ":" + day + " (" + hour + ":" + min + ")-" + backups.length;
        }
        return null;
    }
}
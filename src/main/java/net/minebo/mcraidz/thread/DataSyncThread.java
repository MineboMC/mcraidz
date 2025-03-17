package net.minebo.mcraidz.thread;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class DataSyncThread extends BukkitRunnable {

    @Override
    public void run() {
        ProfileManager.profiles.forEach(ProfileManager::saveProfile);
        TeamManager.teams.forEach(TeamManager::saveTeam);

        Bukkit.getOnlinePlayers().forEach(player -> {
            if(player.hasPermission("basic.admin")) {
                player.sendMessage(ChatColor.YELLOW + "[DataSync] " + ChatColor.GRAY + "Saved " + ProfileManager.profiles.size() + " profiles and " + TeamManager.teams.size() + ChatColor.GRAY + " teams!");
            }
        });
    }

}

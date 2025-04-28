package net.minebo.mcraidz.thread;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaytimeThread extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Profile profile = ProfileManager.getProfileByPlayer(player);
            if (profile != null) {
                profile.playtime += 1;
            }
        });
    }

}

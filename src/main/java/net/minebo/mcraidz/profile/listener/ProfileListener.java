package net.minebo.mcraidz.profile.listener;

import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        if(ProfileManager.getProfileByUUID(event.getUniqueId()) == null) {
            ProfileManager.registerProfile(new Profile(event.getUniqueId()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(ProfileManager.getProfileByPlayer(event.getPlayer()) != null) {
            Profile profile = ProfileManager.getProfileByPlayer(event.getPlayer());

            ProfileManager.saveProfile(profile);
        }
    }

}

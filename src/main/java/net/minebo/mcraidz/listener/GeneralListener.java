package net.minebo.mcraidz.listener;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.util.format.NumberFormatting;
import net.minebo.kregions.KRegions;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Region;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.team.construct.TeamRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GeneralListener implements Listener {

    // Join Message Listener
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        player.sendMessage(ChatColor.DARK_PURPLE + "Welcome, " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.DARK_PURPLE + " to " + ChatColor.LIGHT_PURPLE + "MCRaidz" + ChatColor.DARK_PURPLE + "!");
        player.sendMessage(ChatColor.YELLOW + "This is our " + ChatColor.GOLD + NumberFormatting.addSuffix(1) + ChatColor.YELLOW + " map, which started on " + ChatColor.GOLD + "3/17/2025" + ChatColor.YELLOW + "!");

        if(TeamManager.getTeamByPlayer(player) != null){
            Team team = TeamManager.getTeamByPlayer(player);
            TeamRole teamRole = team.getRole(player.getUniqueId());

            team.sendMessageToMembers(ChatColor.GREEN + "Member Online: " + ChatColor.GOLD + teamRole.prefix + ChatColor.YELLOW + player.getName());
        }

        if(ProfileManager.getProfileByPlayer(player) != null) {
            if (ProfileManager.getProfileByPlayer(player).dieOnLogin) {
                ProfileManager.getProfileByPlayer(player).toggleDieOnLogin();
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.setHealth(0.0);
                player.sendMessage(ChatColor.RED + "You died because your logger was killed while you were offline.");
            }
        }

    }

    // Leave Message Listener
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        if(TeamManager.getTeamByPlayer(player) != null){
            Team team = TeamManager.getTeamByPlayer(player);
            TeamRole teamRole = team.getRole(player.getUniqueId());

            team.sendMessageToMembers(ChatColor.RED + "Member Offline: " + ChatColor.GOLD + teamRole.prefix + ChatColor.YELLOW + player.getName());
        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if(ProfileManager.getProfileByPlayer(e.getPlayer()) != null) {
            Profile profile = ProfileManager.getProfileByPlayer(e.getPlayer());
            Player player = e.getPlayer();

            e.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
            profile.spawnProtection = true;

        }

        if(MCRaidz.cooldownHandler.getCooldown("Enderpearl") != null) {
            Cooldown pearlCooldown = MCRaidz.cooldownHandler.getCooldown("Enderpearl");
            if (pearlCooldown.onCooldown(e.getPlayer())) {
                pearlCooldown.removeCooldown(e.getPlayer());
            }
        }

        if(MCRaidz.cooldownHandler.getCooldown("Combat Tag") != null) {
            Cooldown pvpTagCooldown = MCRaidz.cooldownHandler.getCooldown("Combat Tag");
            if (pvpTagCooldown.onCooldown(e.getPlayer())) {
                pvpTagCooldown.removeCooldown(e.getPlayer());
            }
        }

    }

}

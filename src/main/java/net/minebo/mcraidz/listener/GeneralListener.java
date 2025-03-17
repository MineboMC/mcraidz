package net.minebo.mcraidz.listener;

import net.minebo.cobalt.util.format.NumberFormatting;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.team.construct.TeamRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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

            team.sendMessageToMembers(ChatColor.GREEN + "Member Online: " + ChatColor.GOLD + teamRole.prefix + ChatColor.YELLOW + player.getDisplayName());
        }

    }

}

package net.minebo.mcraidz.cobalt;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.scoreboard.provider.ScoreboardProvider;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardImpl extends ScoreboardProvider {
    @Override
    public String getTitle(Player player){
        return MCRaidz.instance.getConfig().getString("scoreboard.title");
    }

    @Override
    public List<String> getLines(Player player) {

        List<String> lines = new ArrayList<String>();

        Team playerTeam = TeamManager.getTeamByPlayer(player);

        lines.add("&7&m------------------");

        if(playerTeam != null) {
            lines.add("&fTeam: " + ChatColor.GREEN + playerTeam.name);
        }

        Profile profile = ProfileManager.getProfileByPlayer(player);

        if(profile != null) {
            lines.add("&fGold: " + ChatColor.GOLD + "⛃" + ChatColor.YELLOW + profile.getFormattedBalance());
            if(profile.hasSpawnProtection()) lines.add("&aProtected by Spawn");
        }

        if(MCRaidz.cooldownHandler.getCooldown("enderpearl") != null) {
            Cooldown pearlCooldown = MCRaidz.cooldownHandler.getCooldown("enderpearl");
            if (pearlCooldown.onCooldown(player)) {
                lines.add(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Enderpearl" + ChatColor.DARK_AQUA + ": " + ChatColor.WHITE + pearlCooldown.getRemaining(player) + "s");
            }
        }

        if(MCRaidz.cooldownHandler.getCooldown("pvptag") != null) {
            Cooldown pvpTagCooldown = MCRaidz.cooldownHandler.getCooldown("pvptag");
            if (pvpTagCooldown.onCooldown(player)) {
                lines.add(ChatColor.RED + ChatColor.BOLD.toString() + "PvP Tag" + ChatColor.RED + ": " + ChatColor.WHITE + pvpTagCooldown.getRemaining(player) + "s");
            }
        }

        lines.add("");
        lines.add(MCRaidz.instance.getConfig().getString("scoreboard.url"));
        lines.add("&7&m------------------");

        return lines;
    }
}

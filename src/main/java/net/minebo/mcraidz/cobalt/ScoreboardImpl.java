package net.minebo.mcraidz.cobalt;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.scoreboard.provider.ScoreboardProvider;
import net.minebo.cobalt.util.ServerUtil;
import net.minebo.cobalt.util.format.TimeFormatting;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.classes.ClassManager;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.server.ServerHandler;
import net.minebo.mcraidz.server.task.SpawnTask;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.util.BedrockUtil;
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

        lines.add((BedrockUtil.isOnBedrock(player) ? "" : "&7&m------------------"));

        if(playerTeam != null) {
            lines.add("&fTeam: " + ChatColor.GREEN + playerTeam.name);
        }

        Profile profile = ProfileManager.getProfileByPlayer(player);

        if(profile != null) {
            lines.add("&fGold: " + (BedrockUtil.isOnBedrock(player) ? "" : ChatColor.GOLD + "⛃") + ChatColor.YELLOW + profile.getFormattedBalance());
            if(profile.hasSpawnProtection()) lines.add("&aProtected by Spawn");
        }

        if(ClassManager.activeClass.containsKey(player.getUniqueId())) {
            lines.addAll(generateClassLines(player));
        }

        if(getSpawnTeleportScore(player) != null) {
            lines.add(ChatColor.BLUE + ChatColor.BOLD.toString() + "Spawn: " + ChatColor.WHITE + getSpawnTeleportScore(player));
        }

        if(MCRaidz.cooldownHandler.getCooldown("Enderpearl") != null) {
            Cooldown pearlCooldown = MCRaidz.cooldownHandler.getCooldown("Enderpearl");
            if (pearlCooldown.onCooldown(player)) {
                lines.add(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Enderpearl" + ChatColor.DARK_AQUA + ": " + ChatColor.WHITE + pearlCooldown.getRemaining(player));
            }
        }

        if(MCRaidz.cooldownHandler.getCooldown("Combat Tag") != null) {
            Cooldown pvpTagCooldown = MCRaidz.cooldownHandler.getCooldown("Combat Tag");
            if (pvpTagCooldown.onCooldown(player)) {
                lines.add(ChatColor.RED + ChatColor.BOLD.toString() + "PvP Tag" + ChatColor.RED + ": " + ChatColor.WHITE + pvpTagCooldown.getRemaining(player));
            }
        }

        if(player.hasMetadata("modmode")){
            lines.add("");
            lines.add(ChatColor.AQUA + "Staff Info:");
            lines.add(ChatColor.GRAY + " * " + ChatColor.RESET + "TPS: " + ServerUtil.getColoredTPS());
            lines.add(ChatColor.GRAY + " * " + ChatColor.RESET + "Vanish: " + (player.hasMetadata("vanish") ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
            lines.add(ChatColor.GRAY + " * " + ChatColor.RESET + "Chat: " + (player.hasMetadata("toggleSC") ? ChatColor.GOLD + "Staff" : ChatColor.YELLOW + "Public"));
        }

        lines.add("");
        lines.add(MCRaidz.instance.getConfig().getString("scoreboard.url"));

        if(!BedrockUtil.isOnBedrock(player)) {
            lines.add("&7&m------------------");
        }

        return lines;
    }

    public String getSpawnTeleportScore(Player player) {
        SpawnTask spawnTask = ServerHandler.getSpawnTasks().get(player.getName());

        if (spawnTask != null) {
            long diffMillis = spawnTask.getSpawnTime() - System.currentTimeMillis();

            if (diffMillis >= 0) {
                return TimeFormatting.getRemaining(diffMillis) + "s"; // Pass ms
            }
        }

        return null;
    }

    public List<String> generateClassLines(Player player) {
        ArrayList lines = new ArrayList<String>();

        Profile profile = ProfileManager.getProfileByPlayer(player);

        switch (ClassManager.activeClass.get(player.getUniqueId())) {

            case BARD -> {
                lines.add("Class Energy: " + ChatColor.AQUA + ClassManager.bardEnergy.get(player.getUniqueId()).getEnergy());

                Cooldown effectCooldown = MCRaidz.cooldownHandler.getCooldown("Bard Effect");

                if(effectCooldown.onCooldown(player)) {
                    lines.add(ChatColor.GREEN + ChatColor.BOLD.toString() + "Bard Effect" + ChatColor.GREEN + ": " + ChatColor.WHITE + effectCooldown.getRemaining(player));
                }
            }

            case MINER -> {
                lines.add("Diamonds: " + ChatColor.AQUA + profile.diamonds);
            }

            case ARCHER -> {
                lines.add("Class Energy: " + ChatColor.AQUA + ClassManager.archerEnergy.get(player.getUniqueId()).getEnergy());

                Cooldown archerSugarCooldown = MCRaidz.cooldownHandler.getCooldown("Archer Sugar");
                Cooldown archerFeatherCooldown = MCRaidz.cooldownHandler.getCooldown("Archer Feather");

                if(archerSugarCooldown.onCooldown(player)) {
                    lines.add(ChatColor.AQUA + ChatColor.BOLD.toString() + "Speed Effect" + ChatColor.AQUA + ": " + ChatColor.WHITE + archerSugarCooldown.getRemaining(player));
                }

                if(archerFeatherCooldown.onCooldown(player)) {
                    lines.add(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Jump Effect" + ChatColor.LIGHT_PURPLE + ": " + ChatColor.WHITE + archerFeatherCooldown.getRemaining(player));
                }
            }

        }

        return lines;
    }

}

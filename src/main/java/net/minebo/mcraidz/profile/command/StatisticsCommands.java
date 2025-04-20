package net.minebo.mcraidz.profile.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;


public class StatisticsCommands extends BaseCommand {

    @CommandAlias("stats|statistics")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onStatsCommand(Player player, @Optional OfflinePlayer offlinePlayer) {

        if(offlinePlayer != null) {
            if(ProfileManager.getProfileByUUID(offlinePlayer.getUniqueId()) != null) {
                Profile profile = ProfileManager.getProfileByUUID(offlinePlayer.getUniqueId());

                List<String> msg = List.of(
                        "",
                        offlinePlayer.getName() + ChatColor.YELLOW + "'s Statistics",
                        "",
                        ChatColor.YELLOW + "Kills: " + ChatColor.RESET + profile.kills,
                        ChatColor.YELLOW + "Deaths: " + ChatColor.RESET + profile.deaths,
                        ""
                );

                msg.forEach(player::sendMessage);
                return;
            } else {
                player.sendMessage(ChatColor.RED + offlinePlayer.getName() + " does not have a profile.");
                return;
            }
        }

        if(ProfileManager.getProfileByPlayer(player) != null) {
            Profile profile = ProfileManager.getProfileByPlayer(player);

            List<String> msg = List.of(
                    "",
                    player.getName() + ChatColor.YELLOW + "'s Statistics",
                    "",
                    ChatColor.YELLOW + "Kills: " + ChatColor.RESET + profile.kills,
                    ChatColor.YELLOW + "Deaths: " + ChatColor.RESET + profile.deaths,
                    ""
            );

            msg.forEach(player::sendMessage);
        } else {
            player.sendMessage(ChatColor.RED + "Your profile hasn't loaded correctly, please try relogging or asking an admin if this continues.");
        }
    }

    @CommandAlias("statstop")
    @Syntax("<kills|deaths> [page]")
    @CommandCompletion("kills|deaths|gold @nothing")
    public void onStatsTopCommand(Player player, @Optional String type, @Optional String pageStr) {
        if (type == null || (!type.equalsIgnoreCase("kills") && !type.equalsIgnoreCase("deaths") && !type.equalsIgnoreCase("gold"))) type = "kills";

        int page = 1;
        try {
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException ignored) {}

        final int entriesPerPage = 10;
        List<Profile> profiles = ProfileManager.getRegisteredProfiles(); // Assumes you have this

        if (profiles == null || profiles.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No stats found.");
            return;
        }

        // Sort profiles
        String finalType = type; // lambda is an ass
        profiles.sort((a, b) -> {
            if (finalType.equalsIgnoreCase("kills")) {
                return Integer.compare(b.kills, a.kills);
            } else if (finalType.equalsIgnoreCase("deaths")) {
                return Integer.compare(b.deaths, a.deaths);
            } else if (finalType.equalsIgnoreCase("gold")) {
                return Double.compare(b.gold, a.gold);
            }
            return 0;
        });

        int totalPages = (int) Math.ceil((double) profiles.size() / entriesPerPage);
        page = Math.max(1, Math.min(page, totalPages));

        int start = (page - 1) * entriesPerPage;
        int end = Math.min(start + entriesPerPage, profiles.size());

        player.sendMessage(ChatColor.YELLOW + "Top " + finalType + " - Page " + page + "/" + totalPages);

        for (int i = start; i < end; i++) {
            Profile p = profiles.get(i);
            String name = Bukkit.getOfflinePlayer(p.uuid).getName(); // You may need to cache or store player names in the Profile class
            int stat = finalType.equalsIgnoreCase("kills") ? p.kills : p.deaths;
            double statGold = -1;

            if(finalType.equalsIgnoreCase("gold")) {
                statGold = p.gold;
            }

            if(statGold == -1) player.sendMessage(ChatColor.GREEN + "#" + (i + 1) + " " + ChatColor.RESET + name + ChatColor.GRAY + ": " + ChatColor.YELLOW + stat);
            else player.sendMessage(ChatColor.GREEN + "#" + (i + 1) + " " + ChatColor.RESET + name + ChatColor.GRAY + ": " + ChatColor.YELLOW + statGold);
        }

        player.sendMessage(ChatColor.YELLOW + "Use /statstop " + finalType + " [page] to view more.");
    }

}

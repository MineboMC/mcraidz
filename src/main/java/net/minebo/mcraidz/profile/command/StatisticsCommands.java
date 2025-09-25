package net.minebo.mcraidz.profile.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minebo.cobalt.util.format.StringFormatting;
import net.minebo.cobalt.util.pagination.PaginatedResult;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;


public class StatisticsCommands extends BaseCommand {

    @CommandAlias("stats|statistics")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onStatsCommand(Player player, @Optional String string) {

        if(string != null) {
            if (ProfileManager.getProfileByName(string) == null) {
                player.sendMessage(ChatColor.RED + string + " does not have a profile.");
                return;
            }

            Profile profile = ProfileManager.getProfileByName(string);

            List<String> msg = List.of(
                    "",
                    profile.lastKnownUsername + ChatColor.YELLOW + "'s Statistics",
                    "",
                    ChatColor.YELLOW + "Kills: " + ChatColor.RESET + profile.kills,
                    ChatColor.YELLOW + "Deaths: " + ChatColor.RESET + profile.deaths,
                    ChatColor.YELLOW + "KillStreak: " + ChatColor.RESET + profile.killStreak,
                    "",
                    ChatColor.YELLOW + "Playtime: " + ChatColor.RESET + profile.getFormattedPlaytime(),
                    ""
            );

            msg.forEach(player::sendMessage);
            return;
        }

        if(ProfileManager.getProfileByPlayer(player) != null) {
            Profile profile = ProfileManager.getProfileByPlayer(player);

            List<String> msg = List.of(
                    "",
                    player.getName() + ChatColor.YELLOW + "'s Statistics",
                    "",
                    ChatColor.YELLOW + "Kills: " + ChatColor.RESET + profile.kills,
                    ChatColor.YELLOW + "Deaths: " + ChatColor.RESET + profile.deaths,
                    ChatColor.YELLOW + "KillStreak: " + ChatColor.RESET + profile.killStreak,
                    "",
                    ChatColor.YELLOW + "Playtime: " + ChatColor.RESET + profile.getFormattedPlaytime(),
                    ""
            );

            msg.forEach(player::sendMessage);
        } else {
            player.sendMessage(ChatColor.RED + "Your profile hasn't loaded correctly, please try relogging or asking an admin if this continues.");
        }
    }

    @CommandAlias("statstop")
    @Syntax("<kills|deaths|gold|killstreak|playtime> [page]")
    @CommandCompletion("kills|deaths|gold|killstreak|playtime @nothing")
    public void onStatsTopCommand(Player player, @Optional String type, @Optional String pageStr) {
        if (type == null || (!type.equalsIgnoreCase("kills") && !type.equalsIgnoreCase("deaths")
                && !type.equalsIgnoreCase("gold") && !type.equalsIgnoreCase("killstreak")
                && !type.equalsIgnoreCase("playtime"))) {
            type = "kills";
        }

        int page = 1;
        try {
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException ignored) {}

        List<Profile> profiles = ProfileManager.getRegisteredProfiles();

        if (profiles == null || profiles.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No stats found.");
            return;
        }

        String finalType = type.toLowerCase();

        // Build stats map with correct value types
        HashMap<String, Object> statsMap = new HashMap<>();
        for (Profile p : profiles) {
            String name = p.lastKnownUsername;
            switch (finalType) {
                case "kills"      -> statsMap.put(name, p.kills);
                case "deaths"     -> statsMap.put(name, p.deaths);
                case "killstreak" -> statsMap.put(name, p.killStreak);
                case "gold"       -> statsMap.put(name, p.gold);
                case "playtime"   -> statsMap.put(name, p.playtime);
            }
        }

        // Comparator depends on stat type
        Comparator<Map.Entry<String, Object>> comparator = switch (finalType) {
            case "kills", "deaths", "killstreak" ->
                    Comparator.comparingInt(e -> (Integer) e.getValue());
            case "gold" ->
                    Comparator.comparingDouble(e -> (Double) e.getValue());
            case "playtime" ->
                    Comparator.comparingLong(e -> (Long) e.getValue());
            default -> (a, b) -> 0;
        };
        comparator = comparator.reversed();

        PaginatedResult<Object> result = new PaginatedResult<>(statsMap, 10, comparator);

        if (page > result.getTotalPages()) page = result.getTotalPages();
        if (page < 1) page = 1;

        Component header = Component.text("*** ", NamedTextColor.GRAY)
                .append(Component.text("Top " + StringFormatting.fixCapitalization(finalType), NamedTextColor.GOLD))
                .append(Component.text(" (Page " + ChatColor.YELLOW + page + ChatColor.GRAY + "/" + ChatColor.YELLOW + result.getTotalPages() + ChatColor.GRAY + ")", NamedTextColor.GRAY))
                .append(Component.text(" ***", NamedTextColor.GRAY));

        player.sendMessage(header);

        int index = (page - 1) * 10;
        LinkedHashMap<String, Object> pageData = result.getPage(page);

        for (Map.Entry<String, Object> entry : pageData.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            String displayStat;
            if (finalType.equals("playtime")) {
                Profile profile = profiles.stream()
                        .filter(p -> p.lastKnownUsername.equals(name))
                        .findFirst()
                        .orElse(null);
                displayStat = (profile != null) ? profile.getFormattedPlaytime() : value.toString();
            } else {
                displayStat = value.toString();
            }

            player.sendMessage(Component.text(name + ChatColor.GRAY + " - " + ChatColor.YELLOW + displayStat, NamedTextColor.WHITE)
                    .hoverEvent(HoverEvent.showText(Component.text(ChatColor.YELLOW + "Click to view " + ChatColor.RESET + name + ChatColor.YELLOW + "'s stats.")))
                    .clickEvent(ClickEvent.runCommand("/stats " + name)));
        }

        if (result.hasMultiplePages()) {
            player.sendMessage("");
            player.sendMessage(ChatColor.YELLOW + "Use /statstop " + finalType + " [page] to view more.");
            player.sendMessage("");
        }
    }


}

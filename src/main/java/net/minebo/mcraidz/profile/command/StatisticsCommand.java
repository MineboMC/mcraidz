package net.minebo.mcraidz.profile.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("stats|statistics")
public class StatisticsCommand extends BaseCommand {

    @Default
    @CatchUnknown
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
}

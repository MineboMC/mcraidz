package net.minebo.mcraidz.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import org.bukkit.entity.Player;

public class MCRaidzPlaceholderExpansion extends PlaceholderExpansion implements Relational {
    @Override
    public String getIdentifier() {
        return "mcraidz";
    }

    @Override
    public String getAuthor() {
        return "Ian Rich";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String query) {
        if (query.equalsIgnoreCase("nametag")) {
            return getFactionTag(player, player); // use player as both sender and viewer
        }
        return "";
    }

    @Override
    public String onPlaceholderRequest(Player viewer, Player target, String query) {

        if(query.equalsIgnoreCase("nametag")){
            return getFactionTag(viewer, target);
        }

        return "";
    }

    public String getFactionTag(Player viewer, Player target) {
        Team viewerFaction = TeamManager.getTeamByPlayer(viewer);
        Team targetFaction = TeamManager.getTeamByPlayer(target);

        if (targetFaction == null) {

            if(ProfileManager.getProfileByPlayer(target) != null) {
                Profile profile = ProfileManager.getProfileByPlayer(target);

                if(profile.hasSpawnProtection()){
                    return "&e";
                }
            }

            return "&c";
        }

        if (viewerFaction == null) {
            return "&c";
        }

        if (viewer.getUniqueId().equals(target.getUniqueId()) || viewerFaction.name.equalsIgnoreCase(targetFaction.name)) {
            return "&2";
        }

        return "&c";

    }

}
package net.minebo.mcraidz.team;

import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.util.Logger;
import org.bukkit.entity.Player;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamManager {
    public static List<Team> teams;

    public static void init() {

        // Initiate
        teams = new ArrayList<Team>();

        // Load team data here -- TODO: future task

    }

    public static void registerTeam(Team team){
        Logger.log("Registered new team: \"" + team.name + "\"");
        teams.add(team);
    }

    public static void unRegisterTeam(Team team){
        Logger.log("Unregistered team: \"" + team.name + "\"");
        teams.remove(team);
    }

    public static Team getTeamByUUID(UUID uuid){
        for(Team team : teams){
            if (team.members.containsKey(uuid)) {
                return team;
            }
        }

        return null;
    }

    public static Team getTeamByName(String query){
        for(Team team : teams){
            if (team.name.equalsIgnoreCase(query)) {
                return team;
            }
        }

        return null;
    }

    public static Team getTeamByPlayer(Player player){
        for(Team team : teams){
            if (team.members.containsKey(player.getUniqueId())) {
                return team;
            }
        }

        return null;
    }

}

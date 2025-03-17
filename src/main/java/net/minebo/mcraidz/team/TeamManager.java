package net.minebo.mcraidz.team;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minebo.cobalt.gson.Gson;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.team.construct.TeamRole;
import net.minebo.mcraidz.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.time.Instant;
import java.util.*;

public class TeamManager {

    public static List<Team> teams;

    public static File teamsFolder = new File(MCRaidz.instance.getDataFolder(), "internal/profiles");

    public static void init() {

        // Initiate
        teams = new ArrayList<Team>();

        // Load team data here!
        // Check if the directory exists
        if (!teamsFolder.exists()) {
            // If the directory doesn't exist, create it
            boolean created = teamsFolder.mkdirs();

            if (created) {
                System.out.println("Directory created successfully: " + teamsFolder);
            } else {
                System.out.println("Failed to create the directory.");
            }
        } else {
            System.out.println("Directory already exists: " + teamsFolder);
        }

        scanAndLoadTeamData(); // Big Function
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

    public static void scanAndLoadTeamData() {
        // Get all files in the profiles folder
        File[] files = teamsFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            // Loop through each JSON file and load the profile
            for (File file : files) {
                // Load each profile by its file name (UUID)
                loadTeam(file);
            }
        }
    }

    public static void saveTeam(Team team) {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", team.uuid.toString());
        json.addProperty("name", team.name);
        json.addProperty("announcement", team.announcement);
        json.addProperty("password", team.password);
        json.addProperty("creationTime", team.creationTime.toString());

        if (team.headquarters != null) {
            json.add("headquarters", Gson.GSON.toJsonTree(team.headquarters));
        }
        if (team.rally != null) {
            json.add("rally", Gson.GSON.toJsonTree(team.rally));
        }

        JsonArray invitedArray = new JsonArray();
        for (UUID invitedUuid : team.invited) {
            invitedArray.add(invitedUuid.toString());
        }
        json.add("invited", invitedArray);

        JsonObject membersJson = new JsonObject();
        for (Map.Entry<UUID, TeamRole> entry : team.members.entrySet()) {
            membersJson.addProperty(entry.getKey().toString(), entry.getValue().name());
        }
        json.add("members", membersJson);

        File teamFile = new File(teamsFolder, team.uuid.toString() + ".json");
        try (FileWriter writer = new FileWriter(teamFile)) {
            Gson.GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTeam(File teamFile) {
        try (FileReader reader = new FileReader(teamFile)) {
            JsonObject json = Gson.GSON.fromJson(reader, JsonObject.class);

            UUID teamUuid = UUID.fromString(json.get("uuid").getAsString());
            String name = json.get("name").getAsString();
            String announcement = json.has("announcement") ? json.get("announcement").getAsString() : "";
            String password = json.has("password") ? json.get("password").getAsString() : "";
            Instant creationTime = Instant.parse(json.get("creationTime").getAsString());

            Location headquarters = json.has("headquarters") ? Gson.GSON.fromJson(json.get("headquarters"), Location.class) : null;
            Location rally = json.has("rally") ? Gson.GSON.fromJson(json.get("rally"), Location.class) : null;

            List<UUID> invited = new ArrayList<>();
            JsonArray invitedArray = json.getAsJsonArray("invited");
            for (JsonElement element : invitedArray) {
                invited.add(UUID.fromString(element.getAsString()));
            }

            HashMap<UUID, TeamRole> members = new HashMap<>();
            JsonObject membersJson = json.getAsJsonObject("members");
            for (String memberUuid : membersJson.keySet()) {
                members.put(UUID.fromString(memberUuid), TeamRole.valueOf(membersJson.get(memberUuid).getAsString()));
            }

            Team team = new Team(name, announcement, password, creationTime, teamUuid, headquarters, rally, invited, members);

            teams.add(team);
            System.out.println("Loaded team: " + teamUuid);

        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to load team from file: " + teamFile.getName());
        }
    }

}

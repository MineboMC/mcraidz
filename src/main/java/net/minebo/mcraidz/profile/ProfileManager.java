package net.minebo.mcraidz.profile;

import com.google.gson.JsonObject;
import net.minebo.cobalt.gson.Gson;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.profile.listener.ProfileListener;
import net.minebo.mcraidz.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    public static List<Profile> profiles;

    public static File profilesFolder = new File(MCRaidz.instance.getDataFolder(), "internal/profiles");

    public static void init() {
        profiles = new ArrayList<>();

        // Check if the directory exists
        if (!profilesFolder.exists()) {
            // If the directory doesn't exist, create it
            boolean created = profilesFolder.mkdirs();

            if (created) {
                System.out.println("Directory created successfully: " + profilesFolder);
            } else {
                System.out.println("Failed to create the directory.");
            }
        } else {
            System.out.println("Directory already exists: " + profilesFolder);
        }

        scanAndLoadProfileData(); // Big Function

        Bukkit.getPluginManager().registerEvents(new ProfileListener(), MCRaidz.instance);
    }

    public static List<Profile> getRegisteredProfiles() {
        return profiles;
    }

    public static void registerProfile(Profile profile) {
        Logger.log("Registered profile for: \"" + profile.uuid + "\"");
        profiles.add(profile);
    }

    public static void unRegisterProfile(Profile profile){
        Logger.log("Unregistered profile for: \"" + profile.uuid + "\"");
        profiles.remove(profile);
    }

    public static Profile getProfileByUUID(UUID uuid) {
        for (Profile profile : profiles) {
            if (profile.uuid.equals(uuid)) {
                return profile;
            }
        }
        return null;
    }

    public static Profile getProfileByPlayer(Player player){
        return getProfileByUUID(player.getUniqueId());
    }

    public static void scanAndLoadProfileData() {
        // Get all files in the profiles folder
        File[] files = profilesFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            // Loop through each JSON file and load the profile
            for (File file : files) {
                // Load each profile by its file name (UUID)
                loadProfile(file);
            }
        }
    }

    public static void saveProfile(Profile profile) {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", profile.uuid.toString());
        json.addProperty("gold", profile.gold);

        json.addProperty("kills", profile.kills);
        json.addProperty("deaths", profile.deaths);

        JsonObject warpsJson = new JsonObject();
        for (String warpName : profile.warps.keySet()) {
            warpsJson.add(warpName, Gson.GSON.toJsonTree(profile.warps.get(warpName)));  // Manually serialize warps
        }
        json.add("warps", warpsJson);

        json.addProperty("dieOnLogin", profile.dieOnLogin);

        // Saving the JSON to a file
        File profileFile = new File(profilesFolder, profile.uuid.toString() + ".json");
        try (FileWriter writer = new FileWriter(profileFile)) {
            Gson.GSON.toJson(json, writer);  // Serialize the profile to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProfile(File profileFile) {
        try (FileReader reader = new FileReader(profileFile)) {
            // Parse the JSON file into a JsonObject
            JsonObject json = Gson.GSON.fromJson(reader, JsonObject.class);

            // Extract profile data from JSON
            UUID profileUuid = UUID.fromString(json.get("uuid").getAsString());
            double gold = json.get("gold").getAsDouble();

            // Load warps data (if any)
            HashMap<String, Location> warps = new HashMap<>();
            JsonObject warpsJson = json.getAsJsonObject("warps");
            for (String warpName : warpsJson.keySet()) {
                Location warpLocation = Gson.GSON.fromJson(warpsJson.get(warpName), Location.class);
                warps.put(warpName, warpLocation);
            }

            // Create the Profile object
            Profile profile = new Profile(profileUuid);
            profile.setBalance(gold);
            profile.warps = warps;

            profile.kills = 0;
            profile.deaths = 0;

            if(json.has("kills")) profile.kills = json.get("kills").getAsInt();
            if(json.has("deaths")) profile.deaths = json.get("deaths").getAsInt();

            profile.dieOnLogin = false;

            if(json.has("dieOnLogin")) {
                profile.dieOnLogin = json.get("dieOnLogin").getAsBoolean();
            }

            profiles.add(profile);

            // You can store the loaded profile in a list or a map
            // Example: ProfileManager.profiles.put(profileUuid, profile);
            System.out.println("Loaded profile: " + profileUuid);

        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to load profile from file: " + profileFile.getName());
        }
    }

}

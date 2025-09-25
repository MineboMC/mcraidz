package net.minebo.mcraidz.server;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.server.listener.*;
import net.minebo.mcraidz.server.task.Task;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerHandler {

    public static Map<UUID, Task> spawnTasks;
    public static Map<UUID, Task> noAttackTasks;

    public static void init(){
        spawnTasks = new HashMap<>();
        noAttackTasks = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new SpawnListener(), MCRaidz.instance);
        Bukkit.getPluginManager().registerEvents(new NoAttackListener(), MCRaidz.instance);
    }

    public static void startNoAttackTask(final Player player) {
        Profile profile = ProfileManager.getProfileByPlayer(player);

        if(noAttackTasks.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(noAttackTasks.get(player.getUniqueId()).getTaskId());
        }

        player.sendMessage(ChatColor.GRAY.toString() + "You cannot attack for 10 seconds.");

        BukkitTask taskid = new BukkitRunnable() {

            int seconds = 10;

            @Override
            public void run() {

                seconds--;

                if (seconds == 0) {
                    if (noAttackTasks.containsKey(player.getUniqueId())) {
                        noAttackTasks.remove(player.getUniqueId());

                        player.sendMessage(ChatColor.GRAY + "You can now attack other players.");
                        cancel();
                    }
                }

            }
        }.runTaskTimer(MCRaidz.instance, 30L, 30L);

        noAttackTasks.put(player.getUniqueId(), new Task(taskid.getTaskId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)));
    }

    public static void startSpawnCommandTask(final Player player) {
        Profile profile = ProfileManager.getProfileByPlayer(player);

        player.sendMessage(ChatColor.YELLOW.toString() + "Teleporting you to spawn in 10 seconds.");

        BukkitTask taskid = new BukkitRunnable() {

            int seconds = 10;

            @Override
            public void run() {
                if (player.hasMetadata("frozen")) {
                    player.sendMessage(ChatColor.YELLOW + "Your teleport to spawn has cancelled since you are frozen.");
                    cancel();
                    return;
                }

                seconds--;

                if (seconds == 0) {
                    if (spawnTasks.containsKey(player.getUniqueId())) {
                        spawnTasks.remove(player.getUniqueId());
                        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        player.sendMessage(ChatColor.YELLOW + "You have been teleported to spawn!");

                        player.setHealth(20);
                        player.setFoodLevel(20);
                        player.setSaturation(20);

                        if(!profile.hasSpawnProtection()) {
                            profile.spawnProtection = true;
                            player.sendMessage(ChatColor.GREEN + "Your spawn protection has been enabled.");
                        }

                        cancel();
                    }
                }

            }
        }.runTaskTimer(MCRaidz.instance, 30L, 30L);

        spawnTasks.put(player.getUniqueId(), new Task(taskid.getTaskId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)));
    }

    public static Pair<String, String> getLeaderboardPlacementByStat(String stat, int placement) {
        // Defensive copy if you don't want to mutate the original list, otherwise sort in place
        List<Profile> profiles = ProfileManager.getRegisteredProfiles();

        profiles.sort((a, b) -> {
            switch (stat.toLowerCase()) {
                case "kills":
                    return Integer.compare(b.kills, a.kills);
                case "deaths":
                    return Integer.compare(b.deaths, a.deaths);
                case "gold":
                    return Double.compare(b.gold, a.gold);
                case "killstreak":
                    return Integer.compare(b.killStreak, a.killStreak);
                case "playtime":
                    return Long.compare(b.playtime, a.playtime);
                default:
                    return 0;
            }
        });

        if (placement < 1 || placement > profiles.size()) {
            return null;
        }

        Profile p = profiles.get(placement - 1);
        String statValue;
        switch (stat.toLowerCase()) {
            case "kills":
                statValue = String.valueOf(p.kills);
                break;
            case "deaths":
                statValue = String.valueOf(p.deaths);
                break;
            case "gold":
                statValue = String.valueOf(ChatColor.GOLD + "⛃" + ChatColor.YELLOW + p.getFormattedBalance()); // Cast to int for Pair<Integer>
                break;
            case "killstreak":
                statValue = String.valueOf(p.killStreak);
                break;
            case "playtime":
                statValue = String.valueOf(p.getFormattedPlaytime()); // Cast to int for Pair<Integer>
                break;
            default:
                statValue = ChatColor.RED + "None";
        }

        return Pair.of(p.lastKnownUsername, statValue);
    }
}

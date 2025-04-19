package net.minebo.mcraidz.server;

import lombok.Getter;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.server.listener.SpawnListener;
import net.minebo.mcraidz.server.task.SpawnTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerHandler {
    @Getter
    private static Map<String, SpawnTask> spawnTasks;

    public static void init(){
        spawnTasks = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new SpawnListener(), MCRaidz.instance);
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
                    if (spawnTasks.containsKey(player.getName())) {
                        spawnTasks.remove(player.getName());
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
        }.runTaskTimer(MCRaidz.instance, 20L, 20L);

        spawnTasks.put(player.getName(), new SpawnTask(taskid.getTaskId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)));
    }
}

package net.minebo.mcraidz.server.listener;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.server.ServerHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpawnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        if (ServerHandler.spawnTasks.containsKey(event.getPlayer().getUniqueId())) {
            MCRaidz.instance.getServer().getScheduler().cancelTask(ServerHandler.spawnTasks.get(event.getPlayer().getUniqueId()).getTaskId());
            ServerHandler.spawnTasks.remove(event.getPlayer().getUniqueId());
            player.sendMessage(ChatColor.YELLOW + "Your teleport to spawn has cancelled since you moved.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (ServerHandler.spawnTasks.containsKey(player.getUniqueId())) {
                MCRaidz.instance.getServer().getScheduler().cancelTask(ServerHandler.spawnTasks.get(player.getUniqueId()).getTaskId());
                ServerHandler.spawnTasks.remove(player.getUniqueId());
                player.sendMessage(ChatColor.YELLOW + "Your teleport to spawn has cancelled since you took damage.");
            }
        }
    }
}
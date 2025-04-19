package net.minebo.mcraidz.cobalt.cooldown;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.*;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatTagTimer extends Cooldown {

    private final Map<UUID, Entity> chickenLoggers = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;

        Player victim = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();

        applyCooldown(victim, 30l, MCRaidz.instance);
        applyCooldown(attacker, 30l, MCRaidz.instance);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!onCooldown(p)) return;

        // Spawn a chicken logger
        Location loc = p.getLocation();
        World world = p.getWorld();
        Chicken chicken = world.spawn(loc, Chicken.class);
        chicken.setCustomName(p.getName() + ChatColor.YELLOW + "'s Logger");
        chicken.setCustomNameVisible(true);
        chicken.setInvulnerable(false);
        chicken.setAI(false);

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GOLD + "Chicken Logger: " + ChatColor.YELLOW + p.getDisplayName()));

        // Store inventory as metadata or external map
        chicken.getPersistentDataContainer().set(new NamespacedKey("mcraidz", "logger"), PersistentDataType.STRING, p.getUniqueId().toString());
        chickenLoggers.put(p.getUniqueId(), chicken);

        // Schedule despawn task
        Bukkit.getScheduler().runTaskLater(MCRaidz.instance, () -> {
            if (chicken.isValid() && !chicken.isDead()) {
                chicken.remove();
                chickenLoggers.remove(p.getUniqueId());
                // Player keeps items, they survived
            }
        }, 200L); // 10 seconds
    }

    @EventHandler
    public void onChickenLoggerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Chicken)) return;
        Chicken chicken = (Chicken) e.getEntity();

        String uuidStr = chicken.getPersistentDataContainer().get(new NamespacedKey("mcraidz", "logger"), PersistentDataType.STRING);
        if (uuidStr == null) return;

        UUID uuid = UUID.fromString(uuidStr);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        // Player dies for real
        Profile profile = ProfileManager.getProfileByUUID(offlinePlayer.getUniqueId());
        assert profile != null;
        profile.dieOnLogin = true;

        chickenLoggers.remove(uuid);
    }

}

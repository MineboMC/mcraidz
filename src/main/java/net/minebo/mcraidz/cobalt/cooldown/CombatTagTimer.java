package net.minebo.mcraidz.cobalt.cooldown;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.cobalt.data.LoggerData;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CombatTagTimer extends Cooldown {

    private final Map<UUID, Entity> chickenLoggers = new HashMap<>();
    private static final Map<UUID, LoggerData> loggerDataMap = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        Player attacker = null;

        // Arrow (projectile) case
        if (e.getDamager() instanceof Arrow arrow) {
            if (arrow.getShooter() instanceof Player shooter) {
                attacker = shooter;
            }
        }
        // Melee case
        else if (e.getDamager() instanceof Player playerDamager) {
            attacker = playerDamager;
        }

        // Not player-vs-player
        if (attacker == null || !(e.getEntity() instanceof Player victim)) return;

        Profile victimProfile = ProfileManager.getProfileByPlayer(victim);
        Profile attackerProfile = ProfileManager.getProfileByPlayer(attacker);

        if (victimProfile.hasSpawnProtection() || attackerProfile.hasSpawnProtection()) return;

        applyCooldown(victim, 30, TimeUnit.SECONDS, MCRaidz.instance);
        applyCooldown(attacker, 30, TimeUnit.SECONDS, MCRaidz.instance);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!onCooldown(p)) return;

        // Spawn a chicken logger
        Location loc = p.getLocation();
        World world = p.getWorld();
        Chicken chicken = world.spawn(loc, Chicken.class, spawned -> {
            spawned.setCustomName(p.getName() + ChatColor.YELLOW + "'s Logger");
            spawned.setCustomNameVisible(true);
            spawned.setInvulnerable(false);
            spawned.setAI(false);
        });

        loggerDataMap.put(p.getUniqueId(), new LoggerData(
                p.getInventory().getContents(),
                p.getInventory().getArmorContents()
        ));

        Bukkit.broadcastMessage(ChatColor.RED + "[CombatTag] " + p.getName() + " has logged out with combat tag and spawned a logger!");

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
        }, 300L); // 10 seconds
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // If the player has a chicken logger, remove it when they log back in
        if (!chickenLoggers.containsKey(uuid)) return;

        Entity chicken = chickenLoggers.remove(uuid);
        if (chicken != null && !chicken.isDead()) {
            chicken.remove();
        }

        loggerDataMap.remove(uuid); // Cleanup stored data
    }

    @EventHandler
    public void onChickenLoggerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Chicken)) return;
        Chicken chicken = (Chicken) e.getEntity();

        // Check if this is a logger chicken
        String uuidStr = chicken.getPersistentDataContainer().get(new NamespacedKey("mcraidz", "logger"), PersistentDataType.STRING);
        if (uuidStr == null) return;

        if(chicken.getKiller() == null) return;

        // Proceed with death logic
        UUID uuid = UUID.fromString(uuidStr);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        chickenLoggers.remove(uuid);
        LoggerData data = loggerDataMap.remove(uuid);

        if (data != null) {
            Location loc = chicken.getLocation();
            for (ItemStack item : data.contents) {
                if (item != null && item.getType() != Material.AIR)
                    loc.getWorld().dropItemNaturally(loc, item);
            }
            for (ItemStack item : data.armor) {
                if (item != null && item.getType() != Material.AIR)
                    loc.getWorld().dropItemNaturally(loc, item);
            }
        }

        // Mark player to die on login
        Profile profile = ProfileManager.getProfileByUUID(offlinePlayer.getUniqueId());
        if (profile != null) {
            profile.toggleDieOnLogin();
        }

        ProfileManager.getProfileByPlayer(chicken.getKiller()).addKill();
        profile.addDeath();

        Bukkit.broadcastMessage(ChatColor.RED + "[CombatTag] " + offlinePlayer.getName() + " has died while logged out!");
    }
}

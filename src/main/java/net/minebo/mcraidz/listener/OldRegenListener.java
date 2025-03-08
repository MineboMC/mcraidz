package net.minebo.mcraidz.listener;

import net.minebo.mcraidz.MCRaidz;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class OldRegenListener implements Listener {

    private final Map<Player, Long> lastDamageTime = new HashMap<>();
    private final int regenDelay = 10 * 20; // 10 seconds in ticks

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player && event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            event.setCancelled(true);
            scheduleRegen(player);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            lastDamageTime.put(player, System.currentTimeMillis());
        }
    }

    private void scheduleRegen(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                    return;
                }

                Long lastDamage = lastDamageTime.get(player);
                if (lastDamage == null || (System.currentTimeMillis() - lastDamage < regenDelay * 50L)) {
                    return;
                }

                double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (player.getHealth() < maxHealth) {
                    player.setHealth(Math.min(player.getHealth() + 1, maxHealth));
                    scheduleRegen(player);
                }
            }
        }.runTaskLater(MCRaidz.instance, regenDelay);
    }
}

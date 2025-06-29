package net.minebo.mcraidz.deathmessage.listener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.deathmessage.DeathMessageConfiguration;
import net.minebo.mcraidz.deathmessage.DeathMessageHandler;
import net.minebo.mcraidz.deathmessage.damage.Damage;
import net.minebo.mcraidz.deathmessage.damage.PlayerDamage;
import net.minebo.mcraidz.deathmessage.damage.UnknownDamage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEarly(PlayerDeathEvent event) {
        Damage deathCause;
        List<Damage> record = DeathMessageHandler.getDamage(event.getEntity());
        if (!record.isEmpty() && (deathCause = record.get(record.size() - 1)) instanceof PlayerDamage && deathCause.getTimeAgoMillis() < TimeUnit.MINUTES.toMillis(1L)) {
            UUID killerUuid = ((PlayerDamage) deathCause).getDamager();
            Player killerPlayer = Bukkit.getPlayer(killerUuid);
            if (killerPlayer != null && !event.getEntity().isDead()) {
                // Simulate a small amount of damage attributed to the killer
                event.getEntity().damage(0.01, killerPlayer);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerDeathLate(PlayerDeathEvent event) {
        List<Damage> record = DeathMessageHandler.getDamage(event.getEntity());
        Damage deathCause = !record.isEmpty() ? record.get(record.size() - 1) : new UnknownDamage(event.getEntity().getUniqueId(), 1.0);
        DeathMessageHandler.clearDamage(event.getEntity());
        event.setDeathMessage(null);
        DeathMessageConfiguration configuration = DeathMessageHandler.getConfiguration();
        UUID diedUuid = event.getEntity().getUniqueId();
        UUID killerUuid = event.getEntity().getKiller() == null ? null : event.getEntity().getKiller().getUniqueId();
        for (Player player : MCRaidz.instance.getServer().getOnlinePlayers()) {
            boolean showDeathMessage = configuration.shouldShowDeathMessage(player.getUniqueId(), diedUuid, killerUuid);
            if (!showDeathMessage) continue;
            String deathMessage = deathCause.getDeathMessage(player.getUniqueId());
            player.sendMessage(ChatColor.DARK_RED + "☠ " + deathMessage);
        }
    }

}


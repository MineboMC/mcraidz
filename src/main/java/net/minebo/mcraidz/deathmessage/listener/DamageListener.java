package net.minebo.mcraidz.deathmessage.listener;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.deathmessage.DeathMessageHandler;
import net.minebo.mcraidz.deathmessage.damage.UnknownDamage;
import net.minebo.mcraidz.deathmessage.event.CustomPlayerDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class DamageListener implements Listener {

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            CustomPlayerDamageEvent customEvent = new CustomPlayerDamageEvent(event);
            customEvent.setTrackerDamage(new UnknownDamage(player.getUniqueId(), customEvent.getDamage()));
            MCRaidz.instance.getServer().getPluginManager().callEvent(customEvent);
            DeathMessageHandler.addDamage(player, customEvent.getTrackerDamage());
        }
    }

}


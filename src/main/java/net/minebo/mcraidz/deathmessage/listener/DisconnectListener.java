package net.minebo.mcraidz.deathmessage.listener;

import net.minebo.mcraidz.deathmessage.DeathMessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class DisconnectListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DeathMessageHandler.clearDamage(event.getPlayer());
    }

}


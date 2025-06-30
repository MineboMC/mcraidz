package net.minebo.mcraidz.server.listener;

import net.minebo.mcraidz.server.ServerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NoAttackListener implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        if(ServerHandler.noAttackTasks.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

}

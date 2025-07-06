package net.minebo.mcraidz.server.listener;

import net.minebo.mcraidz.server.ServerHandler;
import org.bukkit.entity.Arrow;
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

    @EventHandler
    public void onAttackWithArrow(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getDamager();
        if(!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();

        if(ServerHandler.noAttackTasks.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

}

package net.minebo.mcraidz.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {

    @EventHandler
    public void onUseSoup(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if player is holding mushroom soup
        if (item != null && item.getType() == Material.MUSHROOM_STEW) {
            double maxHealth = 20.0;
            double healAmount = 7.0;

            // Only heal if player's health is below 20
            if (player.getHealth() < maxHealth) {
                double newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
                player.setHealth(newHealth);

                // Consume the soup and replace it with an empty bowl
                player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL));
            }
        }
    }

}
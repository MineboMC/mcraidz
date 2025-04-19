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

            double maxHunger = 20.0;
            double foodAmount = 7.0;

            double newHunger = 0.0;
            double newHealth = 0.0;

            if(player.getFoodLevel() < maxHunger) {
                newHunger = Math.min(player.getFoodLevel() + foodAmount, maxHunger);
                player.setFoodLevel((int) newHunger);
            }
            // Only heal if player's health is below 20
            if (player.getHealth() < maxHealth) {
                newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
                player.setHealth(newHealth);
            }
            if(newHunger != 0.0 || newHealth != 0.0) {
                // Consume the soup and replace it with an empty bowl
                player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL));
            }
        }
    }

}
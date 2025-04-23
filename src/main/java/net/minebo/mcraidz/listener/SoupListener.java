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

            int maxHunger = 20;
            int foodAmount = 7;

            int oldFoodLevel = player.getFoodLevel();
            int newFoodLevel = Math.min(oldFoodLevel + foodAmount, maxHunger);
            float saturationGain = (newFoodLevel - oldFoodLevel) * 0.6f; // Example multiplier

            boolean consumed = false;

            // Restore hunger
            if (oldFoodLevel < maxHunger) {
                player.setFoodLevel(newFoodLevel);
                // Adjust saturation (capped at current food level)
                float newSaturation = Math.min(player.getSaturation() + saturationGain, newFoodLevel);
                player.setSaturation(newSaturation);
                consumed = true;
            }

            // Heal if necessary
            if (player.getHealth() < maxHealth) {
                double newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
                player.setHealth(newHealth);
                consumed = true;
            }

            // Replace soup with bowl
            if (consumed) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL));
            }
        }
    }
}

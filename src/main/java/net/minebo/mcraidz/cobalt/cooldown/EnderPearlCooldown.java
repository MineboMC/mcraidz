package net.minebo.mcraidz.cobalt.cooldown;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.mcraidz.MCRaidz;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderPearlCooldown extends Cooldown {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem().getType() == Material.AIR) {
                return;
            }

            if (event.getItem().getType() == Material.ENDER_PEARL) {
                event.getPlayer().setCooldown(Material.ENDER_PEARL, 0);

                if (this.onCooldown(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You are currently on ender pearl cooldown for " + ChatColor.BOLD + getRemaining(event.getPlayer()) + "s" + ChatColor.RED + "!");
                    event.setCancelled(true);
                } else {
                    applyCooldown(event.getPlayer(), 16L, MCRaidz.instance);
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "You are now on enderpearl cooldown for 16 seconds.");
                }
            }
        }
    }

}

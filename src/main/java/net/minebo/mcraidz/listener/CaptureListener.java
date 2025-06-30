package net.minebo.mcraidz.listener;

import net.minebo.cobalt.util.format.StringFormatting;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class CaptureListener implements Listener {

    @EventHandler
    public void onEggHitMob(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;
        if (event.getHitEntity() == null) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        Entity hitEntity = event.getHitEntity();
        EntityType type = hitEntity.getType();

        Material spawnEgg = Material.getMaterial(type.name() + "_SPAWN_EGG");
        if (spawnEgg == null) return;

        player.sendMessage(ChatColor.GREEN + "You have captured a " + ChatColor.YELLOW + StringFormatting.fixCapitalization(hitEntity.getType().getName().replaceAll("_", " ")) + ChatColor.GREEN + ".");

        hitEntity.getWorld().dropItemNaturally(hitEntity.getLocation(), new ItemStack(spawnEgg));

        hitEntity.remove();
    }

    @EventHandler
    public void onSkeletonSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.SKELETON) return;
        if (event.getLocation().getWorld().getEnvironment() != World.Environment.NETHER) return;

        double chance = 0.2; // 20% chance
        if (Math.random() < chance) {
            // Replace skeleton with wither skeleton
            Location loc = event.getLocation();
            event.getEntity().remove();
            loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
        }
    }

}

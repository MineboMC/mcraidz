package net.minebo.mcraidz.classes.listeners;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.classes.ClassType;
import net.minebo.mcraidz.classes.ClassManager;
import net.minebo.mcraidz.classes.objects.Energy;
import net.minebo.mcraidz.classes.objects.MinerUpgrade;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClassListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_ORE) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            if (!ClassManager.placed.remove(block.getLocation().toString())) {
                Profile profile = ProfileManager.getProfileByUUID(player.getUniqueId());
                int diamondsMined = profile.diamonds;
                int count = 1;
                ClassManager.placed.add(block.getLocation().toString());

                for(int x = -5; x < 5; ++x) {
                    for(int y = -5; y < 5; ++y) {
                        for(int z = -5; z < 5; ++z) {
                            Block otherBlock = block.getLocation().clone().add(x, y, z).getBlock();
                            if (!otherBlock.equals(block) && otherBlock.getType() == Material.DIAMOND_ORE && !ClassManager.placed.contains(otherBlock.getLocation().toString())) {
                                ++count;
                                ClassManager.placed.add(otherBlock.getLocation().toString());
                            }
                        }
                    }
                }

                Bukkit.broadcastMessage("[FD]" + player.getDisplayName() + " just found " + ChatColor.AQUA + count + ChatColor.WHITE + " diamond" + (count == 1 ? "" : "s") + ".");
                if (ClassManager.activeClass.get(player.getUniqueId()) == ClassType.MINER) {
                    MinerUpgrade upgrade = MinerUpgrade.getLevelBasedOnDiamonds(diamondsMined);
                    if (upgrade.getDiamondsNeeded() <= 0 || ClassManager.minerUpgrades.getOrDefault(player.getUniqueId(), MinerUpgrade.NONE) == upgrade) {
                        return;
                    }

                    Bukkit.broadcastMessage(ChatColor.AQUA + "[MINING]" + player.getDisplayName() + ChatColor.GRAY + " has upgraded to " + ChatColor.AQUA + ChatColor.UNDERLINE + "Miner Upgrade " + StringUtils.capitalize(upgrade.name().toLowerCase()) + ChatColor.GRAY + "!");
                    String var10001 = String.valueOf(ChatColor.GREEN);
                    player.sendMessage(var10001 + "You have upgraded to Miner Upgrade " + StringUtils.capitalize(upgrade.name().toLowerCase()) + "!");
                    ClassManager.deactiveClass(player, false);
                    ClassManager.minerUpgrades.put(player.getUniqueId(), upgrade);
                    Bukkit.getScheduler().runTaskLater(MCRaidz.instance, () -> ClassManager.activateClass(player, ClassType.MINER), 5L);
                }

                profile.addDiamonds(1);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_ORE) {
            ClassManager.placed.add(event.getBlock().getLocation().toString());
        }

    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for(Block block : event.getBlocks()) {
            if (block.getType() == Material.DIAMOND_ORE) {
                ClassManager.placed.add(block.getLocation().toString());
            }
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity var5 = event.getDamager();
        if (var5 instanceof Arrow arrow) {
            ProjectileSource var12 = arrow.getShooter();
            if (var12 instanceof Player damager) {
                Entity var13 = event.getEntity();
                if (var13 instanceof Player player) {
                    if (ProfileManager.getProfileByPlayer(player).hasSpawnProtection() || !ClassManager.activeClass.containsKey(damager.getUniqueId()) || ClassManager.activeClass.get(damager.getUniqueId()) != ClassType.ARCHER || ClassManager.activeClass.containsKey(player.getUniqueId()) && ClassManager.activeClass.get(player.getUniqueId()) == ClassType.ARCHER || player == damager) {
                        return;
                    }

                    Team team = TeamManager.getTeamByPlayer(damager);
                    if (team != null && team.getOnlineMembers().contains(player)) {
                        return;
                    }

                    damager.sendMessage(ChatColor.RED + "You have archer tagged " + player.getDisplayName() + ChatColor.RED + "! All inflicted damage will be for " + ChatColor.YELLOW + "50%" + ChatColor.RED + " more!");
                    player.sendMessage(ChatColor.RED + "You have been archer tagged by " + damager.getDisplayName() + ChatColor.RED + "!");
                    Cooldown archerTag = MCRaidz.cooldownHandler.getCooldown("Archer Tag");
                    archerTag.applyCooldown(player, 15L, TimeUnit.SECONDS, MCRaidz.instance);
                }
            }
        }

        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player victim = (Player) event.getEntity();
                Cooldown archerTag = MCRaidz.cooldownHandler.getCooldown("Archer Tag");
                if (archerTag.onCooldown(victim)) {
                    event.setDamage(event.getDamage() * (double)1.5F);
                }
            }
        }

    }

    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Uncomment for debug: Check if event is firing
        //player.sendMessage("DEBUG: Interact event fired! Action: " + event.getAction());

        if (ClassManager.activeClass.containsKey(player.getUniqueId())
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
                && player.getInventory().getItemInMainHand().getType() != Material.AIR) {

            if (player.hasMetadata("ability_used")) return; // prevent duplicate
            player.setMetadata("ability_used", new FixedMetadataValue(MCRaidz.instance, true));

            Bukkit.getScheduler().runTaskLater(MCRaidz.instance, () -> {
                player.removeMetadata("ability_used", MCRaidz.instance);
            }, 1L); // remove metadata in next tick becuz spigot be whiny

            event.setCancelled(true);

            switch (ClassManager.activeClass.get(player.getUniqueId())){

                case ARCHER -> {
                    handleArcherAbility(player);
                }

                case BARD -> {
                    handleBardAbility(player);
                }
            }

        }
    }

    private void handleArcherAbility(Player player) {
        Energy energy = ClassManager.archerEnergy.get(player.getUniqueId());
        if (energy == null) return; // Safety check
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Material type = mainHand.getType();
        switch (type) {
            case SUGAR -> {
                Cooldown archerSugar = MCRaidz.cooldownHandler.getCooldown("Archer Sugar");
                if (archerSugar.onCooldown(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot use this for another " + archerSugar.getRemaining(player) + '.');
                    return;
                }
                if (energy.getEnergy() >= 25) {
                    archerSugar.applyCooldown(player, 25, TimeUnit.SECONDS, MCRaidz.instance);
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.archerEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - 25);
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 4));
                    Bukkit.getScheduler().runTaskLater(MCRaidz.instance, () -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 2));
                    }, 165L);
                    player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.AQUA + "Speed V" + ChatColor.YELLOW + " for 8 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (25 - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case FEATHER -> {
                Cooldown archerFeather = MCRaidz.cooldownHandler.getCooldown("Archer Feather");
                if (archerFeather.onCooldown(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot use this for another " + archerFeather.getRemaining(player) + '.');
                    return;
                }
                if (energy.getEnergy() >= 25) {
                    archerFeather.applyCooldown(player, 25L, TimeUnit.SECONDS, MCRaidz.instance);
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.archerEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - 25);
                    player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 160, 4));
                    player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.LIGHT_PURPLE + "Jump Boost V" + ChatColor.YELLOW + " for 8 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (25 - energy.getEnergy()) + " more energy to use this.");
                }
            }
            default -> {
                // Do nothing
            }
        }
    }

    private void handleBardAbility(Player player) {
        if (ProfileManager.getProfileByPlayer(player).hasSpawnProtection()) {
            player.sendMessage(ChatColor.RED + "You cannot use bard effects while spawn protected.");
            return;
        }
        var bardCooldown = MCRaidz.cooldownHandler.getCooldown("Bard Effect");
        if (bardCooldown.onCooldown(player)) {
            player.sendMessage(ChatColor.RED + "You cannot use another bard effect for " + bardCooldown.getRemaining(player) + '.');
            return;
        }
        Team team = TeamManager.getTeamByPlayer(player);
        List<Player> nearbyMembers = new ArrayList<>();
        if (team != null) {
            for (Player onlinePlayer : team.getOnlineMembers()) {
                if (onlinePlayer != null && onlinePlayer.getLocation().distance(player.getLocation()) <= 20) {
                    nearbyMembers.add(onlinePlayer);
                }
            }
        } else {
            nearbyMembers.add(player);
        }
        Energy energy = ClassManager.bardEnergy.get(player.getUniqueId());
        if (energy == null) return; // Safety
        boolean effectDone = false;
        int cost = 0;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Material type = mainHand.getType();
        switch (type) {
            case SUGAR -> {
                cost = 25;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Player nearbyMember : nearbyMembers) {
                        gaveTo += 1;
                        addBardClickablePotionEffect(nearbyMember, new PotionEffect(PotionEffectType.SPEED, 240, 2));
                    }
                    if ((gaveTo == 0 || gaveTo == 1) && (team != null && team.getOnlineMembers().size() > 1)) {
                        player.sendMessage(ChatColor.RED + "Nobody else got your bard effects!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + (gaveTo - 1) + ChatColor.YELLOW + " nearby teammate" + (gaveTo != 1 ? "s" : "") + " have " + ChatColor.AQUA + "Speed III" + ChatColor.YELLOW + " for 12 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case BLAZE_POWDER -> {
                cost = 60;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Player nearbyMember : nearbyMembers) {
                        if (nearbyMember == player) continue;
                        addBardClickablePotionEffect(nearbyMember, new PotionEffect(PotionEffectType.STRENGTH, 240, 3));
                        gaveTo += 1;
                    }
                    if ((gaveTo == 0 || gaveTo == 1) && (team != null && team.getOnlineMembers().size() > 1)) {
                        player.sendMessage(ChatColor.RED + "Nobody else got your bard effects!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + (gaveTo - 1) + ChatColor.YELLOW + " nearby teammate" + (gaveTo > 1 ? "s" : "") + " have " + ChatColor.RED + "Strength III" + ChatColor.YELLOW + " for 12 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case IRON_INGOT -> {
                cost = 40;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Player nearbyMember : nearbyMembers) {
                        addBardClickablePotionEffect(nearbyMember, new PotionEffect(PotionEffectType.RESISTANCE, 240, 2));
                        gaveTo += 1;
                    }
                    if ((gaveTo == 0 || gaveTo == 1) && (team != null && team.getOnlineMembers().size() > 1)) {
                        player.sendMessage(ChatColor.RED + "Nobody else got your bard effects!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + (gaveTo - 1) + ChatColor.YELLOW + " nearby teammate" + (gaveTo > 1 ? "s" : "") + " have " + ChatColor.GOLD + "Resistance III" + ChatColor.YELLOW + " for 12 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case FEATHER -> {
                cost = 20;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Player nearbyMember : nearbyMembers) {
                        addBardClickablePotionEffect(nearbyMember, new PotionEffect(PotionEffectType.JUMP_BOOST, 240, 4));
                        gaveTo += 1;
                    }
                    if ((gaveTo == 0 || gaveTo == 1) && (team != null && team.getOnlineMembers().size() > 1)) {
                        player.sendMessage(ChatColor.RED + "Nobody else got your bard effects!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + (gaveTo - 1) + ChatColor.YELLOW + " nearby teammate" + (gaveTo > 1 ? "s" : "") + " have " + ChatColor.LIGHT_PURPLE + "Jump Boost V" + ChatColor.YELLOW + " for 12 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case MAGMA_CREAM -> {
                cost = 35;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Player nearbyMember : nearbyMembers) {
                        addBardClickablePotionEffect(nearbyMember, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60, 0));
                        gaveTo += 1;
                    }
                    if ((gaveTo == 0 || gaveTo == 1) && (team != null && team.getOnlineMembers().size() > 1)) {
                        player.sendMessage(ChatColor.RED + "Nobody else got your bard effects!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + (gaveTo - 1) + ChatColor.YELLOW + " nearby teammate" + (gaveTo > 1 ? "s" : "") + " have " + ChatColor.RED + "Fire Resistance" + ChatColor.YELLOW + " for 60 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case GHAST_TEAR -> {
                cost = 45;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Player nearbyMember : nearbyMembers) {
                        addBardClickablePotionEffect(nearbyMember, new PotionEffect(PotionEffectType.REGENERATION, 240, 4));
                        gaveTo += 1;
                    }
                    if ((gaveTo == 0 || gaveTo == 1) && (team != null && team.getOnlineMembers().size() > 1)) {
                        player.sendMessage(ChatColor.RED + "Nobody else got your bard effects!");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + (gaveTo - 1) + ChatColor.YELLOW + " nearby teammate" + (gaveTo > 1 ? "s" : "") + " have " + ChatColor.LIGHT_PURPLE + "Regeneration V" + ChatColor.YELLOW + " for 12 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            case SPIDER_EYE -> {
                cost = 50;
                if (energy.getEnergy() >= cost) {
                    effectDone = true;
                    removeItemOrSetEmpty(player, mainHand);
                    ClassManager.bardEnergy.get(player.getUniqueId()).setEnergy(energy.getEnergy() - cost);
                    int gaveTo = 0;
                    for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                        if (!(entity instanceof Player targetPlayer)) continue;
                        // Don't hit spawn-protected or teammates
                        if (ProfileManager.getProfileByPlayer(targetPlayer).hasSpawnProtection())
                            continue;
                        if (team != null && team.getOnlineMembers().contains(targetPlayer))
                            continue;
                        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 240, 1));
                        gaveTo += 1;
                    }
                    if (gaveTo == 0) {
                        player.sendMessage(ChatColor.RED + "No enemies got withered.");
                        return;
                    }
                    player.sendMessage(ChatColor.WHITE.toString() + gaveTo + ChatColor.YELLOW + " nearby " + (gaveTo > 1 ? "enemies" : "enemy") + " have been " + ChatColor.WHITE + "withered" + ChatColor.YELLOW + " for 12 seconds!");
                } else {
                    player.sendMessage(ChatColor.RED + "You need " + (cost - energy.getEnergy()) + " more energy to use this.");
                }
            }
            default -> {
                // Do nothing
            }
        }
        if (effectDone) {
            MCRaidz.cooldownHandler.getCooldown("Bard Effect").applyCooldown(player, 10, TimeUnit.SECONDS, MCRaidz.instance);
        }
    }

    // Remove 1 item or set hand to empty
    private void removeItemOrSetEmpty(Player player, ItemStack item) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }

    // Apply or override a potion effect for bard clickable abilities
    private void addBardClickablePotionEffect(Player player, PotionEffect toGive) {
        if (!player.hasPotionEffect(toGive.getType())) {
            player.addPotionEffect(toGive);
            return;
        }
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().equals(toGive.getType())) {
                if (toGive.getAmplifier() < potionEffect.getAmplifier()) {
                    return;
                }
                if (toGive.getAmplifier() == potionEffect.getAmplifier() && toGive.getDuration() < potionEffect.getDuration()) {
                    return;
                }
                player.removePotionEffect(toGive.getType());
                player.addPotionEffect(toGive);
                return;
            }
        }
        // If the effect type was not found, add it
        player.addPotionEffect(toGive);
    }
}
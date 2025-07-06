package net.minebo.mcraidz.listener;

import net.minebo.cobalt.cooldown.CooldownHandler;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Region;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.cobalt.cooldown.CombatTagTimer;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.concurrent.TimeUnit;

public class SpawnProtListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(Bukkit.getPluginManager().isPluginEnabled("kRegions")) {
            if(RegionManager.getRegionByLocation(player.getLocation()) != null) {
                Region rg = RegionManager.getRegionByLocation(player.getLocation());

                if(rg.containsFlag(FlagManager.getFlagByName("SafeZone"))){
                    if(ProfileManager.getProfileByPlayer(player) != null) {
                        Profile profile = ProfileManager.getProfileByPlayer(player);

                        profile.spawnProtection = true;

                        player.sendMessage(ChatColor.GREEN + "You are protected by spawn!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveSpawn(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(Bukkit.getPluginManager().isPluginEnabled("kRegions")) {
            if (ProfileManager.getProfileByPlayer(player) != null) {
                Profile profile = ProfileManager.getProfileByPlayer(player);
                if(profile.hasSpawnProtection()) {
                    if (RegionManager.getRegionByLocation(player.getLocation()) != null) {
                        Region rg = RegionManager.getRegionByLocation(player.getLocation());
                        if (!rg.containsFlag(FlagManager.getFlagByName("SafeZone"))) {
                            profile.spawnProtection = false;
                            player.sendMessage(ChatColor.RED + "Your spawn protection has been broken!");
                        }
                    } else {
                        profile.spawnProtection = false;
                        player.sendMessage(ChatColor.RED + "Your spawn protection has been broken!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player attacker = (Player) e.getDamager();
            Profile attackerProfile = ProfileManager.getProfileByPlayer(attacker);

            if(e.getEntity().getType() == EntityType.PLAYER) {
                Player target = (Player) e.getEntity();
                Profile targetProfile = ProfileManager.getProfileByPlayer(target);

                if(targetProfile.hasSpawnProtection()) {
                    e.setCancelled(true);
                    attacker.sendMessage(ChatColor.RED + "This player currently has spawn protection!");
                } else if(!targetProfile.hasSpawnProtection() && attackerProfile.hasSpawnProtection()) {
                    attackerProfile.spawnProtection = false;
                    attacker.sendMessage(ChatColor.RED + "Your spawn protection has been broken!");

                    if(MCRaidz.cooldownHandler.getCooldown("Combat Tag") != null) {
                        MCRaidz.cooldownHandler.getCooldown("Combat Tag").applyCooldown(attacker, 30, TimeUnit.SECONDS, MCRaidz.instance);
                        MCRaidz.cooldownHandler.getCooldown("Combat Tag").applyCooldown(target, 30, TimeUnit.SECONDS, MCRaidz.instance);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent e){
        if(e.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            Profile profile = ProfileManager.getProfileByPlayer(player);
            if(profile.hasSpawnProtection()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSaturation(FoodLevelChangeEvent e){
        if(e.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            Profile profile = ProfileManager.getProfileByPlayer(player);
            if(profile.hasSpawnProtection()) {
                e.setCancelled(true);
                player.setFoodLevel(20);
                player.setSaturation(20f);
            }
        }
    }
}

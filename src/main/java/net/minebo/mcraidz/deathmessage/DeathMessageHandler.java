package net.minebo.mcraidz.deathmessage;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.NoArgsConstructor;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.deathmessage.damage.Damage;
import net.minebo.mcraidz.deathmessage.listener.*;
import net.minebo.mcraidz.deathmessage.tracker.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.*;

@NoArgsConstructor
public final class DeathMessageHandler {

    private static DeathMessageConfiguration configuration = DeathMessageConfiguration.DEFAULT_CONFIGURATION;
    private static final Map<UUID, List<Damage>> damage = new HashMap<>();
    private static boolean initiated = false;

    public static void init() {
        Preconditions.checkState(!initiated);
        initiated = true;
        PluginManager pluginManager = MCRaidz.instance.getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), MCRaidz.instance);
        pluginManager.registerEvents(new DeathListener(), MCRaidz.instance);
        pluginManager.registerEvents(new DisconnectListener(), MCRaidz.instance);
        pluginManager.registerEvents(new GeneralTracker(), MCRaidz.instance);
        pluginManager.registerEvents(new PvPTracker(), MCRaidz.instance);
        pluginManager.registerEvents(new EntityTracker(), MCRaidz.instance);
        pluginManager.registerEvents(new FallTracker(), MCRaidz.instance);
        pluginManager.registerEvents(new ArrowTracker(), MCRaidz.instance);
        pluginManager.registerEvents(new VoidTracker(), MCRaidz.instance);
        pluginManager.registerEvents(new BurnTracker(), MCRaidz.instance);
    }

    public static List<Damage> getDamage(Player player) {
        return damage.containsKey(player.getUniqueId()) ? damage.get(player.getUniqueId()) : ImmutableList.of();
    }

    public static void addDamage(Player player, Damage addedDamage) {
        damage.putIfAbsent(player.getUniqueId(), new ArrayList<>());
        List<Damage> damageList = damage.get(player.getUniqueId());
        while (damageList.size() > 30) {
            damageList.remove(0);
        }
        damageList.add(addedDamage);
    }

    public static void clearDamage(Player player) {
        damage.remove(player.getUniqueId());
    }

    public static DeathMessageConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(DeathMessageConfiguration configuration) {
        DeathMessageHandler.configuration = configuration;
    }
}


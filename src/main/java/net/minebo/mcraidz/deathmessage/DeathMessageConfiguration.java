package net.minebo.mcraidz.deathmessage;

import net.minebo.mcraidz.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface DeathMessageConfiguration {

    DeathMessageConfiguration DEFAULT_CONFIGURATION = new DeathMessageConfiguration(){

        @Override
        public boolean shouldShowDeathMessage(UUID checkFor, UUID died, UUID killer) {
            return true;
        }

        @Override
        public String formatPlayerName(UUID player) {
            Player p = Bukkit.getPlayer(player);
            Integer kills = ProfileManager.getProfileByUUID(player).kills;

            return ChatColor.RED + p.getName() + ChatColor.GRAY + "[" + ChatColor.WHITE + kills + ChatColor.GRAY + "]";
        }
    };

    boolean shouldShowDeathMessage(UUID var1, UUID var2, UUID var3);

    String formatPlayerName(UUID var1);

    default String formatPlayerName(UUID player, UUID formatFor) {
        return this.formatPlayerName(player);
    }

    default boolean hideWeapons() {
        return false;
    }

}


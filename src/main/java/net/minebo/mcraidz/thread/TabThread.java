package net.minebo.mcraidz.thread;

import lombok.SneakyThrows;
import net.minebo.mcraidz.MCRaidz;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class TabThread extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', "\n&6&lMinebo Network\n&eThe home of competitive pvp!\n"));
            player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', "\n&5" + MCRaidz.instance.getConfig().getString("server-name") + " &d// &f" + player.getPing() + "ms &8* &dminebo.net\n&d" + Bukkit.getOnlinePlayers().size() + "&f players online\n"));
        });
    }

}

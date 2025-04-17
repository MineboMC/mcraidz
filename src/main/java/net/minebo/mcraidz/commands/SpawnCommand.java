package net.minebo.mcraidz.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.server.ServerHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

    @Default
    public void onSpawnCommand(CommandSender sender) {
        if(sender instanceof Player) {

            Player player = (Player) sender;

            if (player.hasMetadata("frozen")) {
                sender.sendMessage(ChatColor.RED + "You can't teleport while you're frozen!");
                return;
            }

            if (ServerHandler.getSpawnTasks().containsKey(sender.getName())) {
                sender.sendMessage(ChatColor.RED + "You are already teleporting to spawn.");
                return; // dont potato and let them spam spawn
            }

            ServerHandler.startSpawnCommandTask(player);
        }
    }
}

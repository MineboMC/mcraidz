package net.minebo.mcraidz.tracking.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.minebo.mcraidz.tracking.tracker.Tracker;
import net.minebo.mcraidz.tracking.tracker.impl.PermTracker;
import net.minebo.mcraidz.tracking.tracker.impl.TempTracker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

@CommandAlias("track")
public class TrackCommands extends BaseCommand {

    @Default
    @Subcommand("all")
    public void trackAllCommand(Player sender) {
        if (sender.getWorld().getEnvironment() == World.Environment.NETHER) {
            sender.sendMessage(ChatColor.RED + "You cannot track in the nether!");
        } else {
            Block center = sender.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Tracker tracker = null;
            if (center.getType() == Material.DIAMOND_BLOCK) {
                tracker = new PermTracker(sender);
            } else if (center.getType() == Material.OBSIDIAN) {
                tracker = new TempTracker(sender);
            }

            if (tracker == null) {
                sender.sendMessage(ChatColor.RED + "Not a valid tracking compass.");
                return;
            }

            tracker.trackAll();
        }
    }

    @Subcommand("player")
    @CommandCompletion("@players")
    public void trackPlayerCommand(Player sender, OnlinePlayer target) {
        if(target == null) {
            sender.sendMessage(ChatColor.RED + "You need to specify a target.");
            return;
        }

        if (sender.getWorld().getEnvironment() == World.Environment.NETHER) {
            sender.sendMessage(ChatColor.RED + "You cannot track in the nether!");
        } else {
            Block center = sender.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Tracker tracker = null;
            if (center.getType() == Material.DIAMOND_BLOCK) {
                tracker = new PermTracker(sender);
            } else if (center.getType() == Material.OBSIDIAN) {
                tracker = new TempTracker(sender);
            }

            if (tracker == null) {
                sender.sendMessage(ChatColor.RED + "Not a valid tracking compass.");
                return;
            }

            tracker.track(target.getPlayer());
        }
    }
}

package net.minebo.mcraidz.tracking.tracker.impl;

import java.util.Set;
import java.util.TreeSet;

import mkremins.fanciful.FancyMessage;
import net.minebo.mcraidz.tracking.direction.TrackDirection;
import net.minebo.mcraidz.tracking.tracker.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PermTracker implements Tracker {
   private Player player;
   private Block middle;

   public PermTracker(Player player) {
      this.player = player;
      this.middle = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
   }

   public boolean on(Player target, TrackDirection direction) {
      int length = this.count(direction) * 25;
      int playerX = target.getLocation().getBlockX();
      int playerZ = target.getLocation().getBlockZ();
      if (direction == TrackDirection.North && playerZ <= this.middle.getZ() && playerZ >= this.middle.getZ() - length) {
         return true;
      } else if (direction == TrackDirection.East && playerX >= this.middle.getX() && playerX <= this.middle.getX() + length) {
         return true;
      } else if (direction == TrackDirection.South && playerZ >= this.middle.getZ() && playerZ <= this.middle.getZ() + length) {
         return true;
      } else {
         return direction == TrackDirection.West && playerX <= this.middle.getX() && playerX >= this.middle.getX() - length;
      }
   }

   public void track(Player target) {
      int north = this.count(TrackDirection.North) * 25;
      int east = this.count(TrackDirection.East) * 25;
      int south = this.count(TrackDirection.South) * 25;
      int west = this.count(TrackDirection.West) * 25;
      if ((north != 0 || east != 0 || south != 0 || west != 0) && this.middle.getType() == Material.DIAMOND_BLOCK) {
         for(TrackDirection direction : TrackDirection.values()) {
            int length = this.count(direction) * 25;
            if (length > 0) {
               if (this.on(target, direction)) {
                  this.player.sendMessage(ChatColor.GREEN + target.getName() + " is within " + length + " blocks " + direction + " of here.");
               } else {
                  this.player.sendMessage(ChatColor.RED + target.getName() + " is NOT within " + length + " blocks " + direction + " of here.");
               }
            }
         }

      } else {
         this.player.sendMessage(ChatColor.RED + "Not a valid tracking compass.");
      }
   }

   public void trackAll() {
      int north = this.count(TrackDirection.North) * 25;
      int east = this.count(TrackDirection.East) * 25;
      int south = this.count(TrackDirection.South) * 25;
      int west = this.count(TrackDirection.West) * 25;
      if (north == 0 && east == 0 && south == 0 && west == 0) {
         this.player.sendMessage(ChatColor.RED + "Not a valid tracking compass.");
      } else {
         for(TrackDirection direction : TrackDirection.values()) {
            int length = this.count(direction) * 25;
            if (length != 0) {
               Set<String> players = new TreeSet();

               for(Player player : Bukkit.getOnlinePlayers()) {
                  if (this.player.canSee(player) && this.on(player, direction) & !player.equals(this.player)) {
                     players.add(player.getDisplayName());
                  }
               }

               FancyMessage message = (new FancyMessage(direction + " (" + length + "): ")).color(ChatColor.DARK_AQUA);
               int i = 0;

               for(String str : players) {
                  if (i == players.size() - 1) {
                     message.then(str).color(ChatColor.GRAY).tooltip(ChatColor.GREEN + "Click here to track " + ChatColor.RESET + Bukkit.getPlayer(str).getName() + ChatColor.GREEN + ".").command("/track player " + ChatColor.stripColor(str));
                  } else {
                     message.then(str).color(ChatColor.GRAY).tooltip(ChatColor.GREEN + "Click here to track " + ChatColor.RESET + Bukkit.getPlayer(str).getName() + ChatColor.GREEN + ".").command("/track player " + ChatColor.stripColor(str)).then(", ");
                  }

                  ++i;
               }

               message.send(this.player);
            }
         }

      }
   }

   public int count(TrackDirection direction) {
      int length = 0;

      for(int i = 1; i < 10000; ++i) {
         Block next = this.middle.getRelative(BlockFace.valueOf(direction.toString().toUpperCase()), i);
         if (next.getType() != Material.OBSIDIAN) {
            if (next.getType() == Material.GOLD_BLOCK) {
               ++length;
            } else {
               length = 0;
            }
            break;
         }

         ++length;
      }

      return length;
   }
}

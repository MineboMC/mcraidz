package net.minebo.mcraidz.tracking.tracker.impl;

import java.util.HashSet;
import java.util.Set;

import net.minebo.mcraidz.tracking.direction.TrackDirection;
import net.minebo.mcraidz.tracking.tracker.Tracker;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TempTracker implements Tracker {
   private Player player;
   private Block middle;

   public TempTracker(Player player) {
      this.player = player;
      this.middle = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
   }

   public boolean on(Player target, TrackDirection direction, int length) {
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

   public int count(TrackDirection direction, boolean b) {
      int length = 0;
      Set<Block> toDelete = new HashSet();

      for(int i = 1; i < 10000; ++i) {
         Block next = this.middle.getRelative(BlockFace.valueOf(direction.toString().toUpperCase()), i);
         if (next.getType() != Material.COBBLESTONE) {
            if (next.getType() == Material.STONE) {
               ++length;
               toDelete.add(next);
               toDelete.add(this.middle);
            } else {
               length = 0;
               toDelete.clear();
            }
            break;
         }

         ++length;
         toDelete.add(next);
      }

      if (b) {
         for(Block block : toDelete) {
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.setType(Material.AIR);
         }
      }

      return length;
   }

   public void track(Player target) {
      int north = this.count(TrackDirection.North, true) * 25;
      int east = this.count(TrackDirection.East, true) * 25;
      int south = this.count(TrackDirection.South, true) * 25;
      int west = this.count(TrackDirection.West, true) * 25;
      if (north == 0 && east == 0 && south == 0 && west == 0) {
         this.player.sendMessage(ChatColor.RED + "Not a valid tracking compass.");
      } else {
         for(TrackDirection direction : TrackDirection.values()) {
            int length = 0;
            if (direction == TrackDirection.North) {
               length = north;
            }

            if (direction == TrackDirection.East) {
               length = east;
            }

            if (direction == TrackDirection.South) {
               length = south;
            }

            if (direction == TrackDirection.West) {
               length = west;
            }

            if (length > 0) {
               if (this.on(target, direction, length)) {
                  this.player.sendMessage(target.getDisplayName() + ChatColor.GREEN + " is within " + length + " blocks " + direction + " of here.");
               } else {
                  this.player.sendMessage(target.getDisplayName() + ChatColor.RED + " isn't within " + length + " blocks " + direction + " of here.");
               }
            }
         }

      }
   }

   public void trackAll() {
      int north = this.count(TrackDirection.North, false) * 25;
      int east = this.count(TrackDirection.East, false) * 25;
      int south = this.count(TrackDirection.South, false) * 25;
      int west = this.count(TrackDirection.West, false) * 25;
      if (north == 0 && east == 0 && south == 0 && west == 0) {
         this.player.sendMessage(ChatColor.RED + "Not a valid tracking compass.");
      } else {
         this.player.sendMessage(ChatColor.RED + "You cannot track all with this type of tracker.");
      }
   }
}

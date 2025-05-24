package net.minebo.mcraidz.classes.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChainmailListener implements Listener {

/*    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        ClassType type = Teams.getInstance().getClassManager().getActiveClass().getOrDefault(event.getPlayer().getUniqueId(), null);
        if (type == null || event.getPlayer().getInventory().getChestplate() == null || event.getPlayer().getInventory().getChestplate().getType() != Material.CHAINMAIL_CHESTPLATE) {
            return;
        }
        Color helmetColor = null;
        switch (Teams.getInstance().getClassManager().getActiveClass().get(event.getPlayer().getUniqueId())) {
            case ILLUSIONIST:
                helmetColor = Color.AQUA;
                break;
            case ROGUE:
                helmetColor = Color.RED;
                break;
            case MAGE:
                helmetColor = Color.YELLOW;
                break;
            case FISHERMAN:
                helmetColor = Color.GREEN;
                break;
        }
        if (helmetColor != null) {
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(event.getPlayer().getEntityId(), 4, CraftItemStack.asNMSCopy(ItemBuilder.from(Material.LEATHER_HELMET).color(helmetColor).build()));
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (event.getPlayer() == players) {
                    continue;
                }
                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);

            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getBlockX() % 25 != 0 || event.getTo().getBlockZ() % 25 != 0) {
            System.out.println(event.getTo().getBlockX() % 25);
            return;
        }
        ClassType type = HCF.getInstance().getClassManager().getActiveClass().getOrDefault(event.getPlayer().getUniqueId(), null);
        if (type == null || event.getPlayer().getInventory().getChestplate() == null || event.getPlayer().getInventory().getChestplate().getType() != Material.CHAINMAIL_CHESTPLATE) {
            return;
        }
        Color helmetColor = null;
        switch (HCF.getInstance().getClassManager().getActiveClass().get(event.getPlayer().getUniqueId())) {
            case ILLUSIONIST:
                helmetColor = Color.AQUA;
                break;
            case ROGUE:
                helmetColor = Color.RED;
                break;
            case MAGE:
                helmetColor = Color.YELLOW;
                break;
            case FISHERMAN:
                helmetColor = Color.GREEN;
                break;
        }
        if (helmetColor != null) {
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(event.getPlayer().getEntityId(), 4, CraftItemStack.asNMSCopy(ItemBuilder.from(Material.LEATHER_HELMET).color(helmetColor).build()));
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (event.getPlayer() == players) {
                    continue;
                }
                ((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);

            }
        }
    }*/
}

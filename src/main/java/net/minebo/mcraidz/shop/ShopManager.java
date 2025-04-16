package net.minebo.mcraidz.shop;

import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.shop.construct.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShopManager {

    public static List<ShopItem> shopItems;
    public static Map<String, ShopItem> itemCache;

    public ShopManager(){
        shopItems = new ArrayList<ShopItem>(); // Update later
        itemCache = new HashMap<String, ShopItem>();
    }

    public static ShopItem getItemByUUID(UUID uuid){
        for(ShopItem item : shopItems){
            if(item.id == uuid){
                return item;
            }
        }
        return null;
    }

    public static void addShopItem(ShopItem item) {
        shopItems.add(item);
        itemCache.put(item.name.toLowerCase(), item);
    }

    public static void removeShopItem(ShopItem item) {
        shopItems.remove(item);
        itemCache.remove(item.name.toLowerCase());
    }

    public static ShopItem getItemByName(String name) {
        return itemCache.get(name.toLowerCase());
    }

    public static boolean buyItemByName(Player sender, String name) {
        ShopItem item = getItemByName(name);
        if (item == null) return false;

        Profile buyerProfile = ProfileManager.getProfileByPlayer(sender);
        if (buyerProfile == null || buyerProfile.getBalance() < item.price) return false;

        buyerProfile.subtractBalance(item.price);

        Profile sellerProfile = ProfileManager.getProfileByUUID(item.owner);
        if (sellerProfile != null) {
            sellerProfile.addBalance(item.price);
        }

        sender.getInventory().addItem(item.item.clone());
        removeShopItem(item);

        sender.sendMessage(ChatColor.GREEN + "Purchased " + item.name + " for " + ChatColor.GOLD + "⛃" + ChatColor.YELLOW + item.price);
        return true;
    }

    public static boolean sellItemInHand(Player player, double price) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType() == Material.AIR) return false;

        ShopItem item = new ShopItem(player.getUniqueId(), price, held.clone());
        addShopItem(item);

        player.getInventory().setItemInMainHand(null);
        player.sendMessage(ChatColor.GREEN + "Listed " + item.name + " for " + ChatColor.GOLD + "⛃" + ChatColor.YELLOW + price);
        return true;
    }
}

package net.minebo.mcraidz.shop;

import com.google.gson.JsonObject;
import net.minebo.cobalt.gson.Gson;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import net.minebo.mcraidz.shop.construct.ShopItem;
import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.util.ItemStackUtil;
import net.minebo.mcraidz.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ShopManager {

    public static List<ShopItem> shopItems;
    public static Map<String, ShopItem> itemCache;

    private static final File shopItemsFolder = new File(MCRaidz.instance.getDataFolder(),"internal/shopItems");

    public static void init(){
        shopItems = new ArrayList<ShopItem>(); // Update later
        itemCache = new HashMap<String, ShopItem>();
        scanAndLoadShopItems();
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
        saveShopItem(item);
    }

    public static void removeShopItem(ShopItem item) {
        shopItems.remove(item);
        itemCache.remove(item.name.toLowerCase());
        deleteShopItem(item);
    }

    public static ShopItem getItemByName(String name) {
        return itemCache.get(name.toLowerCase());
    }

    public static void deleteShopItem(ShopItem item) {
        // Get all files in the profiles folder
        File[] files = shopItemsFolder.listFiles((dir, name) -> name.endsWith(".json"));

        assert files != null;
        for (File file : files) {
            if (file.getName().equals(item.id + ".json")) {  // Use .equals() for string comparison
                file.delete();
            }
        }
    }

    public static void scanAndLoadShopItems() {
        File[] files = shopItemsFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                loadShopItem(file);
            }
        }
    }

    private static void loadShopItem(File file) {
        try (FileReader reader = new FileReader(file)) {
            JsonObject json = Gson.GSON.fromJson(reader, JsonObject.class);

            String name = json.get("name").getAsString();
            double price = json.get("price").getAsDouble();
            UUID owner = UUID.fromString(json.get("owner").getAsString());
            UUID id = UUID.fromString(json.get("id").getAsString());

            // Deserialize ItemStack
            ItemStack item = ItemStackUtil.itemStackFromBase64(json.get("item").getAsString());

            ShopItem shopItem = new ShopItem(name, owner, price, id, item);
            shopItem.id = id;
            shopItem.name = name;

            // Store it however you manage items, e.g., a map:
            // itemsById.put(id, shopItem);
            addShopItem(shopItem);

            System.out.println("Loaded shop item: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to load shop item from: " + file.getName());
        }
    }


    public static void saveShopItem(ShopItem item) {

        if (item.item == null) {
            Bukkit.getLogger().severe("ShopItem " + item.name + " has a null ItemStack.");
            return; // Prevent saving this item
        }

        JsonObject json = new JsonObject();
        json.addProperty("name", item.name);
        json.addProperty("price", item.price);
        json.addProperty("owner", item.owner.toString());
        json.addProperty("id", item.id.toString());
        json.addProperty("item", ItemStackUtil.itemStackToBase64(item.item)); // ItemStack -> Map -> JSON

        File file = new File(shopItemsFolder, item.id.toString() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            Gson.GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean buyItemByName(Player sender, String name, int amount) {
        List<ShopItem> itemsToBuy = new ArrayList<>();
        int found = 0;

        ShopItem sampleItem = getItemByName(name);
        if (sampleItem == null) {
            sender.sendMessage(ChatColor.RED + "That item was not found in the market.");
            return false;
        }

        double price = sampleItem.getPrice();

        for (ShopItem item : new ArrayList<>(shopItems)) {
            if (item.name.equalsIgnoreCase(name)) {
                itemsToBuy.add(item);
                found++;
                if (found == amount) break;
            }
        }

        if (itemsToBuy.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "That item was not found in the market.");
            return false;
        }

        if (itemsToBuy.size() < amount) {
            sender.sendMessage(ChatColor.RED + "Only " + itemsToBuy.size() + " is available in the market.");
            return false;
        }

        double totalCost = price * amount;

        Profile buyerProfile = ProfileManager.getProfileByPlayer(sender);
        if (buyerProfile == null || buyerProfile.getBalance() < totalCost) {
            sender.sendMessage(ChatColor.RED + "You don't have enough balance. Total cost: ⛃" + totalCost);
            return false;
        }

        buyerProfile.subtractBalance(totalCost);

        Map<UUID, Integer> itemsSoldBySeller = new HashMap<>();
        ItemStack base = null;
        int totalAmount = 0;

        for (ShopItem item : itemsToBuy) {
            // Track how many items each seller sold
            itemsSoldBySeller.put(item.owner, itemsSoldBySeller.getOrDefault(item.owner, 0) + 1);

            if (base == null) {
                base = item.item.clone();
                totalAmount = 1;
            } else if (base.isSimilar(item.item)) {
                totalAmount++;
            } else {
                sender.getInventory().addItem(item.item.clone());
                continue;
            }

            removeShopItem(item);
        }

        if (base != null) {
            base.setAmount(totalAmount);
            sender.getInventory().addItem(base);
        }

        // Pay and notify each seller
        for (Map.Entry<UUID, Integer> entry : itemsSoldBySeller.entrySet()) {
            UUID sellerUUID = entry.getKey();
            int soldAmount = entry.getValue();
            double sellerEarnings = soldAmount * price;

            Profile seller = ProfileManager.getProfileByUUID(sellerUUID);
            if (seller != null) seller.addBalance(sellerEarnings);

            Player sellerPlayer = Bukkit.getPlayer(sellerUUID);
            if (sellerPlayer != null) {
                sellerPlayer.sendMessage(sender.getDisplayName() + ChatColor.GRAY + " bought " +
                        (soldAmount == 1 ? "a " : ChatColor.YELLOW + Integer.toString(soldAmount) + ChatColor.GRAY + "x ") +
                        ChatColor.GOLD + name.toUpperCase() + ChatColor.GRAY + " from you for " +
                        ChatColor.GOLD + "⛃" + ChatColor.YELLOW + sellerEarnings + ChatColor.GRAY + ".");
            }
        }

        sender.sendMessage(ChatColor.GRAY + "Purchased " + ChatColor.YELLOW + amount + ChatColor.GRAY + "x " +
                ChatColor.GOLD + name.toUpperCase() + ChatColor.GRAY + " for " +
                ChatColor.GOLD + "⛃" + ChatColor.YELLOW + totalCost + ChatColor.GRAY + ".");
        return true;
    }

    public static boolean sellItemInHand(Player player, double price) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType() == Material.AIR) return false;

        int amount = held.getAmount();
        if (amount <= 0) return false;

        ItemStack single = held.clone();
        single.setAmount(1);

        for (int i = 0; i < amount; i++) {
            ShopItem item = new ShopItem(player.getUniqueId(), price, single.clone());
            addShopItem(item);
        }

        player.getInventory().setItemInMainHand(null);
        player.sendMessage(ChatColor.GRAY + "You sold " + ChatColor.YELLOW + amount + ChatColor.GRAY + "x " + ChatColor.GOLD + ItemUtil.getItemId(held).toUpperCase() + ChatColor.GRAY + " at " + ChatColor.GOLD + "⛃" + ChatColor.YELLOW + price + ChatColor.GRAY + " each.");

        return true;
    }

}

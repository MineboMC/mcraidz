package net.minebo.mcraidz.shop.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import net.minebo.mcraidz.shop.ShopManager;
import net.minebo.mcraidz.shop.construct.ShopItem;
import net.minebo.mcraidz.util.BedrockUtil;
import net.minebo.mcraidz.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("price")
public class PriceCommand extends BaseCommand {

    @Default
    @Syntax("<item>")
    @CommandCompletion("@materials")
    public void onPriceCommand(Player player, String id) {
        String itemId = id.toLowerCase();
        List<ShopItem> allShopItems = ShopManager.shopItems;

        // Track the lowest price and the corresponding item
        double lowestPrice = Double.MAX_VALUE;
        ShopItem foundItem = null;

        // Iterate over all shop items to find the lowest price
        for (ShopItem item : allShopItems) {
            if (item.name.equalsIgnoreCase(itemId)) {
                double itemPrice = item.price;
                if (itemPrice < lowestPrice) {
                    lowestPrice = itemPrice;
                    foundItem = item;
                }
            }
        }

        // If the item is found, display the lowest price
        if (foundItem != null) {
            player.sendMessage(ChatColor.GRAY + "Lowest price for " + ChatColor.GOLD + id.toUpperCase() + ChatColor.GRAY + " is " + (BedrockUtil.isOnBedrock(player) ? "" : ChatColor.GOLD + "⛃") + ChatColor.YELLOW + lowestPrice + ChatColor.GRAY + ".");
        } else {
            player.sendMessage(ChatColor.RED + "Item '" + itemId + "' not found in the shop.");
        }
    }
}

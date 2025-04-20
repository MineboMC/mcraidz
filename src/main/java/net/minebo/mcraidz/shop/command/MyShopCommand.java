package net.minebo.mcraidz.shop.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import net.minebo.mcraidz.shop.ShopManager;
import net.minebo.mcraidz.shop.construct.ShopItem;
import net.minebo.mcraidz.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@CommandAlias("myshop")
public class MyShopCommand extends BaseCommand {

    private static final int ITEMS_PER_PAGE = 5;

    @Default
    @Syntax("[page]")
    public void onMyShop(Player player, @Optional Integer pageOptional) {
        UUID playerUUID = player.getUniqueId();

        List<ShopItem> myItems = ShopManager.shopItems.stream()
                .filter(item -> item.getOwner().equals(playerUUID))
                .collect(Collectors.toList());

        if (myItems.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "You have no items listed in the market.");
            return;
        }

        int totalPages = (int) Math.ceil(myItems.size() / (double) ITEMS_PER_PAGE);
        int page = pageOptional != null ? Math.max(1, Math.min(pageOptional, totalPages)) : 1;

        int start = (page - 1) * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, myItems.size());

        player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------");
        player.sendMessage(ChatColor.GOLD + "Your Shop Listings " + ChatColor.GRAY + "(Page " + page + "/" + totalPages + ")");
        player.sendMessage("");

        for (int i = start; i < end; i++) {
            ShopItem item = myItems.get(i);
            player.sendMessage(ChatColor.DARK_GRAY + "* " + ChatColor.YELLOW + item.getName().toUpperCase() +
                    ChatColor.GRAY + " | Price: " + ChatColor.GOLD + "⛃" + ChatColor.YELLOW + item.getPrice());
        }

        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/myshop <page>" + ChatColor.GRAY + " to view other pages.");
        player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------");
    }
}

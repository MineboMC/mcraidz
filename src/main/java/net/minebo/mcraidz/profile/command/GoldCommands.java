package net.minebo.mcraidz.profile.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class GoldCommands extends BaseCommand {

    @CommandAlias("gold")
    @Syntax("<player>")
    @CommandCompletion("@players")
    public void onGoldCommand(Player sender, @Optional OnlinePlayer player) {
        if(player != null) {
            Profile targetProfile = ProfileManager.getProfileByUUID(player.getPlayer().getUniqueId());

            if (targetProfile == null) {
                sender.sendMessage(ChatColor.RED + player.getPlayer().getName() + ChatColor.RED + " does not have a profile!");
            }

            sender.sendMessage(player.getPlayer().getDisplayName() + ChatColor.YELLOW + "'s Gold: " + ChatColor.GOLD + targetProfile.getBalance() + "⛃");
        } else {
            Profile profile = ProfileManager.getProfileByUUID(sender.getUniqueId());

            if(profile == null) {
                sender.sendMessage(ChatColor.RED + "Your profile hasn't loaded correctly, please try relogging or asking an admin if this continues.");
            }

            sender.sendMessage(ChatColor.YELLOW + "Your Gold: " + ChatColor.GOLD + profile.getBalance() + "⛃");
        }
    }

    @CommandAlias("deposit")
    public void onDepositCommand(Player player) {

        Profile playerProfile = ProfileManager.getProfileByPlayer(player);

        if(playerProfile == null) {
            player.sendMessage(ChatColor.RED + "Your profile hasn't loaded correctly, please try relogging or asking an admin if this continues.");
        }

        var inventory = player.getInventory();
        double totalGold = 0.0;

        double nuggetValue = 0.11;
        double ingotValue = 1.0;
        double blockValue = 9.0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                switch (item.getType()) {
                    case GOLD_NUGGET -> totalGold += nuggetValue * item.getAmount();
                    case GOLD_INGOT -> totalGold += ingotValue * item.getAmount();
                    case GOLD_BLOCK -> totalGold += blockValue * item.getAmount();
                    default -> {}
                }
                inventory.remove(item);
            }
        }

        if (totalGold > 0) {
            playerProfile.addBalance(totalGold);
            player.sendMessage(ChatColor.YELLOW + "Deposited " + ChatColor.GOLD + totalGold + "⛃" + ChatColor.YELLOW + "!");
        } else {
            player.sendMessage(ChatColor.RED + "You have no gold to deposit!");
        }
    }

    @CommandAlias("withdraw")
    @Syntax("<amount>")
    public void onWithdrawCommand(Player player, double amount) {

        Profile playerProfile = ProfileManager.getProfileByPlayer(player);

        if(playerProfile == null) {
            player.sendMessage(ChatColor.RED + "Your profile hasn't loaded correctly, please try relogging or asking an admin if this continues.");
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Illegal withdraw amount, you can't withdraw negative.");
            return;
        }

        if (amount <= 0.11) {
            player.sendMessage(ChatColor.RED + "You can't withdraw less than the value of a gold nugget (0.11)!");
            return;
        }

        if (!playerProfile.subtractBalance(amount)) {
            player.sendMessage(ChatColor.RED + "You don't have enough gold!");
            return;
        }

        var inventory = player.getInventory();
        double remaining = amount;

        while (remaining >= 9.0 && inventory.firstEmpty() != -1) {
            inventory.addItem(new ItemStack(Material.GOLD_BLOCK, 1));
            remaining -= 9.0;
        }
        while (remaining >= 1.0 && inventory.firstEmpty() != -1) {
            inventory.addItem(new ItemStack(Material.GOLD_INGOT, 1));
            remaining -= 1.0;
        }
        while (remaining >= 0.11 && inventory.firstEmpty() != -1) {
            inventory.addItem(new ItemStack(Material.GOLD_NUGGET, 1));
            remaining -= 0.11;
        }

        player.sendMessage(ChatColor.YELLOW + "You withdrew " + ChatColor.GOLD + amount + "⛃" + ChatColor.YELLOW + "!");
    }
}
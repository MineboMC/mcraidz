package net.minebo.mcraidz.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.minebo.cobalt.menu.construct.Button;
import net.minebo.cobalt.menu.construct.Menu;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.util.KitUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

@CommandAlias("testing|kits")
public class TestingCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Default
    public void onTestCommand(CommandSender sender, @Optional OnlinePlayer target) {

        if(!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if(player.hasPermission("basic.admin") && !MCRaidz.instance.getConfig().getBoolean("kits-enabled")) {
            player.sendMessage(ChatColor.GREEN + "You are bypassing the testing requirement for kits.");
            if(target != null) {
                target.getPlayer().sendMessage(ChatColor.GREEN + "An admin has opened the kits gui for you.");
            }
        } else {
            if (!(MCRaidz.instance.getConfig().getBoolean("kits-enabled"))) {
                player.sendMessage(ChatColor.RED + "You can only use this command during testing.");
                return;
            }

            if (!ProfileManager.getProfileByPlayer(player).hasSpawnProtection()) {
                player.sendMessage(ChatColor.RED + "You can only use kits when you are protected by spawn.");
                return;
            }
        }

        new Menu().setTitle("Kits")
                .setSize(9)
                .setAutoUpdate(false)
                .setButton(0, new Button()
                        .setName("&aArcher Kit")
                        .setMaterial(Material.LEATHER_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveArcher(player))
                        .addClickAction(ClickType.LEFT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the " + ChatColor.GREEN + "Archer Kit" + ChatColor.GRAY + "."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(2, new Button()
                        .setName("&eBard Kit")
                        .setMaterial(Material.GOLDEN_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveBard(player))
                        .addClickAction(ClickType.LEFT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the " + ChatColor.YELLOW + "Bard Kit" + ChatColor.GRAY + "."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(4, new Button()
                        .setName("&bDiamond Kit")
                        .setMaterial(Material.DIAMOND_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveDiamond(player))
                        .addClickAction(ClickType.LEFT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the " + ChatColor.AQUA + "Diamond Kit" + ChatColor.GRAY + "."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(6, new Button()
                        .setName("&7Rogue Kit")
                        .setMaterial(Material.CHAINMAIL_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveRogue(player))
                        .addClickAction(ClickType.LEFT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the Rogue Kit."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(8, new Button()
                        .setName("&dFill Inventory with Soup")
                        .setLines(() -> Arrays.asList("&7Left click to refill."))
                        .setMaterial(Material.MUSHROOM_STEW)
                        .addClickAction(ClickType.LEFT, p -> KitUtil.fillWithSoup(player.getInventory()))
                        .addClickAction(ClickType.LEFT, p -> p.sendMessage(ChatColor.GRAY + "Your inventory has been filled with soup."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .fillEmpty(Material.GRAY_STAINED_GLASS_PANE, true)
                .openMenu((target != null ? target.getPlayer() : player));

    }
}

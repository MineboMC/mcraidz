package net.minebo.mcraidz.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
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

@CommandAlias("testing")
public class TestingCommand extends BaseCommand {

    @Default
    public void onTestCommand(CommandSender sender) {

        if(!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if(!(MCRaidz.instance.getConfig().getString("scoreboard.title").contains("Beta"))) {
            player.sendMessage(ChatColor.RED + "You can only use this command during beta.");
            return;
        }

        if(!ProfileManager.getProfileByPlayer(player).hasSpawnProtection()) {
            player.sendMessage(ChatColor.RED + "You can only use kits when you are protected by spawn.");
            return;
        }

        new Menu().setTitle("Kits")
                .setSize(9)
                .setAutoUpdate(false)
                .setButton(0, new Button()
                        .setName("&aArcher Kit")
                        .setMaterial(Material.LEATHER_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveArcher(player))
                        .addClickAction(ClickType.RIGHT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the " + ChatColor.GREEN + "Archer Kit" + ChatColor.GRAY + "."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(2, new Button()
                        .setName("&eBard Kit")
                        .setMaterial(Material.GOLDEN_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveBard(player))
                        .addClickAction(ClickType.RIGHT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the " + ChatColor.YELLOW + "Bard Kit" + ChatColor.GRAY + "."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(4, new Button()
                        .setName("&bDiamond Kit")
                        .setMaterial(Material.DIAMOND_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveDiamond(player))
                        .addClickAction(ClickType.RIGHT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the " + ChatColor.AQUA + "Diamond Kit" + ChatColor.GRAY + "."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(6, new Button()
                        .setName("&7Rogue Kit")
                        .setMaterial(Material.CHAINMAIL_HELMET)
                        .setLines(() -> Arrays.asList("&7Left click to use this kit."))
                        .addClickAction(ClickType.LEFT, p -> KitUtil.giveRogue(player))
                        .addClickAction(ClickType.RIGHT, p -> p.sendMessage(ChatColor.GRAY + "You've been given the Rogue Kit."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .setButton(8, new Button()
                        .setName("&dFill Inventory with Soup")
                        .setLines(() -> Arrays.asList("&7Left click to refill."))
                        .setMaterial(Material.MUSHROOM_STEW)
                        .addClickAction(ClickType.LEFT, p -> KitUtil.fillWithSoup(player.getInventory()))
                        .addClickAction(ClickType.RIGHT, p -> p.sendMessage(ChatColor.GRAY + "Your inventory has been filled with soup."))
                        .addClickAction(ClickType.LEFT, HumanEntity::closeInventory)
                )
                .fillEmpty(Material.GRAY_STAINED_GLASS_PANE)
                .openMenu(player);

    }
}

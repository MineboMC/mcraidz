package net.minebo.mcraidz.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.minebo.mcraidz.MCRaidz;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

@CommandAlias("testing")
public class TestingCommand extends BaseCommand {

    @Default
    public void onTestCommand(CommandSender sender) {

        if(!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if(!(MCRaidz.instance.getConfig().getString("scoreboard.title").contains("Testing"))) {
            player.sendMessage(ChatColor.RED + "You can only use this kit on our testing server.");
            return;
        }

        for (int i = 0; i <= 35; i++) {
            player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_STEW));
        }

        player.getInventory().setItem(0, getSword());

        player.getInventory().setHelmet(getHelmet());
        player.getInventory().setChestplate(getChestplate());
        player.getInventory().setLeggings(getLeggings());
        player.getInventory().setBoots(getBoots());

        player.getInventory().setItem(1, getPearl());

        player.getInventory().setItem(7, getPotionItem(PotionType.STRENGTH));
        player.getInventory().setItem(16, getPotionItem(PotionType.STRENGTH));
        player.getInventory().setItem(25, getPotionItem(PotionType.STRENGTH));
        player.getInventory().setItem(34, getPotionItem(PotionType.STRENGTH));

        player.getInventory().setItem(8, getPotionItem(PotionType.STRONG_SWIFTNESS));
        player.getInventory().setItem(17, getPotionItem(PotionType.STRONG_SWIFTNESS));
        player.getInventory().setItem(26, getPotionItem(PotionType.STRONG_SWIFTNESS));
        player.getInventory().setItem(35, getPotionItem(PotionType.STRONG_SWIFTNESS));

        sender.sendMessage(ChatColor.GREEN + "You find yourself mysteriously flush with items.");
    }

    public ItemStack getPotionItem(PotionType effect) {
        ItemStack potion = new ItemStack(Material.POTION);

        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setBasePotionType(effect);
        potion.setItemMeta(meta);

        return potion;
    }

    public ItemStack getPearl(){
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        item.setAmount(16);

        return item;
    }

    public ItemStack getSword(){
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);

        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.SHARPNESS, 2, true);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getHelmet(){
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);

        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION, 2, true);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getChestplate(){
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);

        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION, 2, true);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getLeggings() {
        ItemStack item = new ItemStack(Material.DIAMOND_LEGGINGS);

        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION, 2, true);

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getBoots(){
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);

        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION, 2, true);

        item.setItemMeta(meta);

        return item;
    }
}

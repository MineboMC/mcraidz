package net.minebo.mcraidz.util;

import net.minebo.cobalt.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class KitUtil {

    public static ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD).addEnchantment(Enchantment.SHARPNESS, 1).addEnchantment(Enchantment.UNBREAKING, 2).build();
    public static ItemStack pearls = new ItemBuilder(Material.ENDER_PEARL).setSize(16).build();

    public static void giveArcher(Player player) {

        player.getInventory().clear();

        Inventory inv = player.getInventory();

        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());

        inv.setItem(0, sword);
        inv.setItem(1, pearls);

        player.getInventory().setItem(8, new ItemBuilder(Material.BOW).addEnchantment(Enchantment.POWER, 2).addEnchantment(Enchantment.INFINITY, 0).addEnchantment(Enchantment.UNBREAKING, 2).setSize(64).build());
        player.getInventory().setItem(17, new ItemBuilder(Material.SUGAR).setSize(64).build());
        player.getInventory().setItem(26, new ItemBuilder(Material.FEATHER).setSize(64).build());
        player.getInventory().setItem(35, new ItemBuilder(Material.ARROW).setSize(1).build());

        fillWithSoup(player.getInventory());

    }

    public static void giveBard(Player player) {

        player.getInventory().clear();

        Inventory inv = player.getInventory();

        player.getInventory().setHelmet(new ItemBuilder(Material.GOLDEN_HELMET).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.GOLDEN_CHESTPLATE).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.GOLDEN_LEGGINGS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setBoots(new ItemBuilder(Material.GOLDEN_BOOTS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());

        inv.setItem(0, sword);
        inv.setItem(1, pearls);

        player.getInventory().setItem(16, new ItemBuilder(Material.SUGAR).setSize(64).build());
        player.getInventory().setItem(25, new ItemBuilder(Material.BLAZE_POWDER).setSize(64).build());
        player.getInventory().setItem(34, new ItemBuilder(Material.FEATHER).setSize(64).build());

        player.getInventory().setItem(17, new ItemBuilder(Material.MAGMA_CREAM).setSize(64).build());
        player.getInventory().setItem(26, new ItemBuilder(Material.IRON_INGOT).setSize(64).build());
        player.getInventory().setItem(35, new ItemBuilder(Material.GHAST_TEAR).setSize(64).build());

        fillWithSoup(player.getInventory());

    }

    public static void giveDiamond(Player player) {

        player.getInventory().clear();

        Inventory inv = player.getInventory();

        player.getInventory().setHelmet(new ItemBuilder(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());

        inv.setItem(0, sword);
        inv.setItem(1, pearls);

        player.getInventory().setItem(7, getPotionItem(PotionType.STRENGTH));
        player.getInventory().setItem(16, getPotionItem(PotionType.STRENGTH));
        player.getInventory().setItem(25, getPotionItem(PotionType.STRENGTH));
        player.getInventory().setItem(34, getPotionItem(PotionType.STRENGTH));

        player.getInventory().setItem(8, getPotionItem(PotionType.STRONG_SWIFTNESS));
        player.getInventory().setItem(17, getPotionItem(PotionType.STRONG_SWIFTNESS));
        player.getInventory().setItem(26, getPotionItem(PotionType.STRONG_SWIFTNESS));
        player.getInventory().setItem(35, getPotionItem(PotionType.STRONG_SWIFTNESS));

        fillWithSoup(player.getInventory());

    }

    public static void giveRogue(Player player) {

        player.getInventory().clear();

        Inventory inv = player.getInventory();

        player.getInventory().setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());
        player.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).addEnchantment(Enchantment.PROTECTION, 2).addEnchantment(Enchantment.UNBREAKING, 2).build());

        inv.setItem(0, sword);
        inv.setItem(1, pearls);

        player.getInventory().setItem(8, ItemStack.of(Material.GOLDEN_SWORD));
        player.getInventory().setItem(17, ItemStack.of(Material.GOLDEN_SWORD));
        player.getInventory().setItem(26, ItemStack.of(Material.GOLDEN_SWORD));
        player.getInventory().setItem(35, ItemStack.of(Material.GOLDEN_SWORD));

        fillWithSoup(player.getInventory());

    }

    public static void fillWithSoup(Inventory inventory) {
        for (int i = 0; i <= 35; i++) {
            if(inventory.getItem(i) == null) inventory.setItem(i, new ItemStack(Material.MUSHROOM_STEW));
        }
    }

    public static ItemStack getPotionItem(PotionType effect) {
        ItemStack potion = new ItemStack(Material.POTION);

        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setBasePotionType(effect);
        potion.setItemMeta(meta);

        return potion;
    }

}

package net.minebo.mcraidz.shop.construct;

import net.minebo.mcraidz.shop.ShopManager;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopItem {
    public String name;
    public UUID owner;
    public Double price;

    public UUID id;

    public ItemStack item;

    public ShopItem(UUID owner, Double price, ItemStack item) {
        this.name = item.getItemMeta().getDisplayName();
        this.price = price;
        this.item = item;
        this.owner = owner;

        this.id = getUnusedUUID(); // Make sure we dont accidentally generate the same twice (should never happen!)
    }

    public UUID getUnusedUUID(){
        UUID testFor = UUID.randomUUID();

        if(ShopManager.getItemByUUID(testFor) != null){
            return getUnusedUUID();
        }

        return testFor;
    }

}

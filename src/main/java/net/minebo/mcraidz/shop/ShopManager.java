package net.minebo.mcraidz.shop;

import net.minebo.mcraidz.shop.construct.ShopItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopManager {

    public static List<ShopItem> shopItems;

    public ShopManager(){
        shopItems = new ArrayList<ShopItem>(); // Update later
    }

    public static ShopItem getItemByUUID(UUID uuid){
        for(ShopItem item : shopItems){
            if(item.id == uuid){
                return item;
            }
        }
        return null;
    }
}

package net.minebo.mcraidz.util;

import org.bukkit.entity.Player;

public class BedrockUtil {

    public static Boolean isOnBedrock(Player player) {
        return (player.getName().charAt(0) == '.');
    }
}

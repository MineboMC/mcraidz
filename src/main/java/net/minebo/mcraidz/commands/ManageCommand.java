package net.minebo.mcraidz.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.minebo.cobalt.menu.construct.Button;
import net.minebo.cobalt.menu.construct.Menu;
import net.minebo.mcraidz.MCRaidz;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

@CommandAlias("manage")
public class ManageCommand extends BaseCommand {

    @Default
    @CommandPermission("basic.admin")
    public void onManageCommand(CommandSender sender) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        new Menu().setTitle("Manage MCRaidz")
                .setSize(9)
                .setAutoUpdate(true)
                .setButton(4,
                        new Button()
                                .setName((MCRaidz.instance.getConfig().getBoolean("kits-enabled") ? "&a&lKits" : "&c&lKits"))
                                .setMaterial(Material.DIAMOND)
                                .setLines("&7Left Click to toggle kits.")
                                .addClickAction(ClickType.LEFT, p -> {
                                    MCRaidz.instance.getConfig().set("kits-enabled", !MCRaidz.instance.getConfig().getBoolean("kits-enabled"));
                                    MCRaidz.instance.saveConfig();
                                })
                )
                .fillEmpty(Material.GRAY_STAINED_GLASS_PANE, true)
                .openMenu(player);

    }
}

package net.minebo.mcraidz.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("mcraidz|author")
public class AuthorCommand extends BaseCommand {

    // Might make this longer later...
    @Default
    public void onAuthorCommand(CommandSender sender) {
        List<String> lines = new ArrayList<>();

        lines.add(ChatColor.YELLOW + "This server is running " + ChatColor.LIGHT_PURPLE + "MCRaidz" + ChatColor.YELLOW + " by " + ChatColor.AQUA + "Ian Rich" + ChatColor.YELLOW + "!");

        lines.forEach(sender::sendMessage);

    }
}

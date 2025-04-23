package net.minebo.mcraidz.profile.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.command.CommandSender;

@CommandAlias("go|warp")
public class WarpCommands extends BaseCommand {

    @Default
    @HelpCommand
    public void help(CommandSender sender) {}
}

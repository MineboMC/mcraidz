package net.minebo.mcraidz;

import net.minebo.cobalt.acf.ACFCommandController;
import net.minebo.cobalt.acf.ACFManager;
import net.minebo.cobalt.cooldown.CooldownHandler;
import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.gson.Gson;
import net.minebo.cobalt.menu.MenuHandler;
import net.minebo.cobalt.scoreboard.ScoreboardHandler;
import net.minebo.mcraidz.classes.ClassManager;
import net.minebo.mcraidz.classes.listeners.ClassListener;
import net.minebo.mcraidz.cobalt.ScoreboardImpl;
import net.minebo.mcraidz.cobalt.completion.TeamCompletionHandler;
import net.minebo.mcraidz.cobalt.completion.WarpCompletionHandler;
import net.minebo.mcraidz.cobalt.context.TeamContextResolver;
import net.minebo.mcraidz.cobalt.cooldown.CombatTagTimer;
import net.minebo.mcraidz.cobalt.cooldown.EnderPearlCooldown;
import net.minebo.mcraidz.hook.MCRaidzPlaceholderExpansion;
import net.minebo.mcraidz.listener.*;
import net.minebo.mcraidz.mongo.MongoManager;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.recipe.RecipeManager;
import net.minebo.mcraidz.server.ServerHandler;
import net.minebo.mcraidz.shop.ShopManager;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import net.minebo.mcraidz.thread.DataSyncThread;
import net.minebo.mcraidz.thread.PlaytimeThread;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCRaidz extends JavaPlugin {

    public static MCRaidz instance;

    public static ACFManager acfManager;
    public static CooldownHandler cooldownHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new MCRaidzPlaceholderExpansion().register(); // We will find a solution when the time comes... - Ian
        }

        cooldownHandler = new CooldownHandler(this);
        cooldownHandler.registerCooldown("Enderpearl", new EnderPearlCooldown());
        cooldownHandler.registerCooldown("Combat Tag", new CombatTagTimer());

        cooldownHandler.registerCooldown("Archer Tag", new Cooldown());
        cooldownHandler.registerCooldown("Archer Sugar", new Cooldown());
        cooldownHandler.registerCooldown("Archer Feather", new Cooldown());

        cooldownHandler.registerCooldown("Rogue Sugar", new Cooldown());
        cooldownHandler.registerCooldown("Rogue Feather", new Cooldown());

        cooldownHandler.registerCooldown("Bard Effect", new Cooldown());

        acfManager = new ACFManager(this);

        ACFCommandController.registerCompletion("teams", new TeamCompletionHandler());
        ACFCommandController.registerCompletion("warps", new WarpCompletionHandler());
        ACFCommandController.registerContext(Team.class, new TeamContextResolver());
        ACFCommandController.registerAll(this);

        Gson.init();

        registerManagers();

        registerListeners();

        new ScoreboardHandler(new ScoreboardImpl(), this);

        MenuHandler.init(this);

        new PlaytimeThread().runTaskTimer(this, 20L, 20L);
        new DataSyncThread().runTaskTimer(this, 20L,  10L * 60L * 20L);

    }

    @Override
    public void onDisable() {
        TeamManager.teams.forEach(TeamManager::saveTeam);
        ProfileManager.profiles.values().forEach(ProfileManager::saveProfile);
    }

    public void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new GeneralListener(), this);
        Bukkit.getPluginManager().registerEvents(new SoupListener(), this);
        Bukkit.getPluginManager().registerEvents(new OldRegenListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatFormatListener(), this);
        Bukkit.getPluginManager().registerEvents(new StatTrackListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnProtListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeamProtListener(), this);
        Bukkit.getPluginManager().registerEvents(new ClassListener(), this);
    }

    public void registerManagers(){
        MongoManager.init(getConfig().getString("mongo.uri"), getConfig().getString("mongo.database"));
        TeamManager.init();
        ProfileManager.init();
        ServerHandler.init();
        ShopManager.init();
        RecipeManager.init();
        ClassManager.init();
    }

}

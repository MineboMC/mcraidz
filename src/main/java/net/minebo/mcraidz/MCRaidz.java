package net.minebo.mcraidz;

import net.minebo.cobalt.acf.ACFCommandController;
import net.minebo.cobalt.acf.ACFManager;
import net.minebo.cobalt.gson.Gson;
import net.minebo.cobalt.scoreboard.ScoreboardHandler;
import net.minebo.mcraidz.cobalt.ScoreboardImpl;
import net.minebo.mcraidz.cobalt.completion.TeamCompletionHandler;
import net.minebo.mcraidz.listener.GeneralListener;
import net.minebo.mcraidz.listener.OldRegenListener;
import net.minebo.mcraidz.listener.SoupListener;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.thread.DataSyncThread;
import net.minebo.mcraidz.thread.TabThread;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCRaidz extends JavaPlugin {

    public static MCRaidz instance;

    public static ACFManager acfManager;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        acfManager = new ACFManager(this);
        ACFCommandController.registerCompletion("teams", new TeamCompletionHandler());

        Gson.init();

        registerManagers();

        registerListeners();

        new ScoreboardHandler(new ScoreboardImpl(), this);

        new TabThread().runTaskTimer(this, 20L, 20L);
        new DataSyncThread().runTaskTimer(this, 20L,  10L * 60L * 20L);

    }

    @Override
    public void onDisable() {
        TeamManager.teams.forEach(TeamManager::saveTeam);
        ProfileManager.profiles.forEach(ProfileManager::saveProfile);
    }

    public void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new GeneralListener(), this);
        Bukkit.getPluginManager().registerEvents(new SoupListener(), this);
        Bukkit.getPluginManager().registerEvents(new OldRegenListener(), this);
    }

    public void registerManagers(){
        TeamManager.init();
        ProfileManager.init();
    }

}

package net.minebo.mcraidz.classes.runnables;

import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.classes.ClassManager;
import net.minebo.mcraidz.classes.ClassType;
import org.bukkit.entity.Player;

public class ClassActivateTask implements Runnable
{
    private Player player;
    private ClassType classType;

    public ClassActivateTask(Player player, ClassType classType) {
        this.player = player;
        this.classType = classType;
    }

    @Override
    public void run() {
        ClassManager.activateClass(player, classType);
    }
}

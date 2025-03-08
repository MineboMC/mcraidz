package net.minebo.mcraidz.util;

import net.minebo.mcraidz.MCRaidz;

import java.util.logging.Level;

public class Logger {
    public static void log(String msg) {
        MCRaidz.instance.getLogger().log(Level.INFO, msg);
    }
}

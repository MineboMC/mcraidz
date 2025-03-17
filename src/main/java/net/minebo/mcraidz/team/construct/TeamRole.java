package net.minebo.mcraidz.team.construct;

public enum TeamRole {
    LEADER("**"),
    CAPTAIN("*"),
    MEMBER("");

    public String prefix;

    TeamRole(String prefix){
        this.prefix = prefix;
    }
}
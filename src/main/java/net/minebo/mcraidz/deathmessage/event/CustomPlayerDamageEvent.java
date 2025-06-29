package net.minebo.mcraidz.deathmessage.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;
import net.minebo.mcraidz.deathmessage.damage.Damage;

public final class CustomPlayerDamageEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final EntityDamageEvent cause;
    private Damage trackerDamage;

    public CustomPlayerDamageEvent(EntityDamageEvent cause) {
        super((Player)cause.getEntity());
        this.cause = cause;
    }

    public double getDamage() {
        return this.cause.getDamage(EntityDamageEvent.DamageModifier.BASE);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public EntityDamageEvent getCause() {
        return this.cause;
    }

    public Damage getTrackerDamage() {
        return this.trackerDamage;
    }

    public void setTrackerDamage(Damage trackerDamage) {
        this.trackerDamage = trackerDamage;
    }
}


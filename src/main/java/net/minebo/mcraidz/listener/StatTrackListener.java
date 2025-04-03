package net.minebo.mcraidz.listener;

import com.google.common.util.concurrent.AbstractScheduledService;
import net.minebo.mcraidz.profile.ProfileManager;
import net.minebo.mcraidz.profile.construct.Profile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


/*
    This is going to be a Listener that updates stats on Profiles.
 */
public class StatTrackListener implements Listener {

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {

        e.setDeathMessage(null);

        if(!(e.getDamageSource() instanceof Player)) {
            return;
        }

        Profile killer = ProfileManager.getProfileByPlayer(e.getEntity());
        Profile slain = ProfileManager.getProfileByPlayer(e.getPlayer());

        if(slain.uuid == killer.uuid) {
            return;
        }

        // I can't wait for this to become a lot more confusing! -- Ian

        killer.kills += 1;
        slain.deaths += 1;

    }

}

package net.minebo.mcraidz.listener;

import net.minebo.mcraidz.team.TeamManager;
import net.minebo.mcraidz.team.construct.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamProtListener implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow)e.getDamager();
            Player p = (Player)e.getEntity();

            if(arrow.getShooter() instanceof Player) {
                if(((Player) arrow.getShooter()).getUniqueId().equals(p.getUniqueId())) {
                    e.setCancelled(true);
                }
            }

        }

        if(e.getDamager().getType() == EntityType.PLAYER) {
            Player attacker = (Player) e.getDamager();

            if(e.getEntity().getType() == EntityType.PLAYER) {
                Player target = (Player) e.getEntity();

                if(TeamManager.getTeamByPlayer(target) != null && TeamManager.getTeamByPlayer(attacker) != null) {
                    Team targetTeam = TeamManager.getTeamByPlayer(target);
                    Team attackerTeam = TeamManager.getTeamByPlayer(attacker);

                    if(targetTeam == attackerTeam) {
                        e.setCancelled(true);
                        attacker.sendMessage(ChatColor.RED + "You can't attack team members!");
                    }
                }
            }
        }
    }
}

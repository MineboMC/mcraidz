package net.minebo.mcraidz.team.construct;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Team {

    // General Info
    public String name;
    public String announcement;
    public String password;

    // Locations
    public Location headquarters;
    public Location rally;

    // Invites
    public List<UUID> invited;

    // Roster
    public HashMap<UUID, TeamRole> members;

    public Team(String name, String password, Player player){
        this.name = name;
        this.password = password;
        this.members = new HashMap<>();
        this.invited = new ArrayList<>();
        members.put(player.getUniqueId(), TeamRole.LEADER);
    }

    public Team(String name, Player player){
        this.name = name;
        this.members = new HashMap<>();
        this.invited = new ArrayList<>();
        members.put(player.getUniqueId(), TeamRole.LEADER);
    }

    public TeamRole getRole(UUID uuid){
        return members.get(uuid);
    }

    public void setRole(UUID uuid, TeamRole role){
        members.put(uuid, role);
    }

    public void sendMessageToMembers(Player sender, String string){
        getOnlineMembers().forEach((player) -> {

            String msg = "&3[&bTeam&3] &b";

            msg += sender.getDisplayName() + "&3: ";
            msg += "&f" + string;

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        });
    }

    public void sendMessageToMembers(String string){
        getOnlineMembers().forEach((player) -> {
            String msg = string;

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        });
    }

    public String generateInfoMessage(){
        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.DARK_AQUA + "*** " + ChatColor.AQUA + name + ChatColor.DARK_AQUA + " ***" + "\n");
        builder.append(ChatColor.GRAY + "Members: " + getPlayersWithRole());

        return builder.toString();
    }

    // a "chatgpt ass solution" -- Kab
    public String getPlayersWithRole() {
        StringBuilder builder = new StringBuilder();

        // Sort members by their TeamRole using compareTo(), which compares enums by their declared order
        List<Map.Entry<UUID, TeamRole>> sortedMembers = members.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue()) // Compare by enum order
                .toList();

        // Iterate through sorted members and build the string
        for (Map.Entry<UUID, TeamRole> entry : sortedMembers) {
            UUID uuid = entry.getKey();
            TeamRole role = entry.getValue();

            Player dummy = Bukkit.getPlayer(uuid);

            // Append role prefix
            builder.append(ChatColor.DARK_GREEN + role.prefix);

            // Append online/offline status
            builder.append((dummy != null && dummy.isOnline()) ? ChatColor.GREEN : ChatColor.RED);

            // Append player name or "Unknown" if null
            builder.append(dummy != null ? dummy.getName() : Bukkit.getOfflinePlayer(uuid).getName());

            // Add a separator (comma and space) for all except the last entry
            builder.append(ChatColor.GRAY).append(", ");
        }

        // Remove the trailing ", " if the builder is not empty
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 4); // Remove last ", "
        }

        return builder.toString();
    }

    public List<Player> getOnlineMembers(){
        List<Player> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            if(members.containsKey(player.getUniqueId())){
                players.add(player);
            }
        }
        return players;
    }

}

package net.minebo.mcraidz.profile.construct;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import net.minebo.cobalt.gson.Gson;
import net.minebo.mcraidz.MCRaidz;
import net.minebo.mcraidz.profile.ProfileManager;
import org.bson.json.JsonParseException;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

@AllArgsConstructor
public class Profile {

    public UUID uuid;
    public Double gold;

    public Integer kills = 0;
    public Integer deaths = 0;

    public HashMap<String, Location> warps;

    public Boolean dieOnLogin = false;

    public Boolean spawnProtection = false;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.gold = 0.00;
        this.warps = new HashMap<>();
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            gold += amount;
        }
    }

    public boolean subtractBalance(double amount) {
        if (amount > 0 && gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void setBalance(double amount) {
        if (amount >= 0) {
            gold = amount;
        }
    }

    public double getBalance() {
        return gold;
    }

    public double getFormattedBalance() {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(gold));
    }

    public Boolean hasSpawnProtection() {
        return spawnProtection;
    }

    public void toggleDieOnLogin() {
        dieOnLogin = !dieOnLogin;
        ProfileManager.saveProfile(this);
    }

    public void addKill() {
        kills++;
        ProfileManager.saveProfile(this);
    }

    public void addDeath() {
        deaths++;
        ProfileManager.saveProfile(this);
    }

}

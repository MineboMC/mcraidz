package net.minebo.mcraidz.profile.construct;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import net.minebo.cobalt.gson.Gson;
import net.minebo.mcraidz.MCRaidz;
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

    public HashMap<String, Location> warps;

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

}

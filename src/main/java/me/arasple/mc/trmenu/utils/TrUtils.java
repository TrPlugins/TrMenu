package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Numbers;
import me.arasple.mc.trmenu.action.TrAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * @author Arasple
 * @date 2020/1/14 21:11
 * TrUtils can be used in expressions and JavaScript
 */
public class TrUtils {

    private static TrUtils instance = new TrUtils();

    public static TrUtils getInst() {
        return instance;
    }


    /*
    TRACTION
     */

    public void runAction(Player player, String... actions) {
        for (String action : actions) {
            if (Strings.isEmpty(action)) {
                continue;
            }
            TrAction.runActions(TrAction.readActions(action), player);
        }
    }

    /*
    BUNGEE
     */

    public void connect(Player player, String server) {
        Bungees.connect(player, server);
    }

    public void sendBungeeData(Player player, String... data) {
        Bungees.sendBungeeData(player, data);
    }

    /*
    PLAYERS
     */

    public boolean isPlayerOnline(String player) {
        return Bukkit.getPlayerExact(player) != null && Bukkit.getPlayerExact(player).isOnline();
    }

    public Player getPlayer(String player) {
        return Bukkit.getPlayerExact(player);
    }

    public Player getRandomPlayer() {
        return new ArrayList<>(Bukkit.getOnlinePlayers()).get(randomInteger(0, Bukkit.getOnlinePlayers().size() - 1));
    }


    /*
    NUMBER
     */

    public int randomInteger(int low, int high) {
        return Numbers.getRandomInteger(low, high);
    }

    public double randomDouble(double low, double high) {
        return Numbers.getRandomDouble(low, high);
    }

    public boolean isNumber(String number) {
        return NumberUtils.isParsable(number);
    }

    public boolean chance(double rate) {
        return Numbers.random(rate <= 1 ? rate : rate / 100);
    }

    /*
    LOCATIONS
     */

    public Location createLocation(String world, double x, double z) {
        double y = Bukkit.getWorld(world).getHighestBlockYAt((int) x, (int) z);
        return createLocation(world, x, y, z);
    }

    public Location createLocation(String world, double x, double y, double z) {
        return createLocation(world, x, y, z, 0, 0);
    }

    public Location createLocation(String world, double x, double y, double z, float yaw, float pitch) {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public Location createLocation(World world, double x, double z) {
        double y = world.getHighestBlockYAt((int) x, (int) z);
        return createLocation(world, x, y, z);
    }

    public Location createLocation(World world, double x, double y, double z) {
        return createLocation(world, x, y, z, 0, 0);
    }

    public Location createLocation(World world, double x, double y, double z, float yaw, float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }


}

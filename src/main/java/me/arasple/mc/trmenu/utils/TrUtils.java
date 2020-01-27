package me.arasple.mc.trmenu.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.lite.Numbers;
import me.arasple.mc.trmenu.action.TrAction;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public String parseBracketPlaceholders(Player player, String string) {
        return PlaceholderAPI.setBracketPlaceholders(player, string);
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

    public String[] getPlayerArgs(String player) {
        return ArgsCache.getPlayerArgs(getPlayer(player));
    }

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
    ITEMSTACKS
     */

    public ItemBuilder getItemBuildr() {
        return new ItemBuilder(Material.STONE);
    }

    /*
    INVENTORY
     */

    public static <T> List<List<T>> readList(Object object, Class<T> classz) {
        List<List<T>> list = Lists.newArrayList();
        if (object == null) {
            return list;
        } else if (!(object instanceof List) || ((List) object).size() <= 0) {
            list.add(Arrays.asList(classz.cast(object)));
        } else if (((List) object).get(0) instanceof List) {
            ((List) object).forEach(x -> list.add((ArrayList<T>) x));
        } else {
            list.add(castList(object, classz));
        }
        return list;
    }

    public int getEmptySlot(Inventory inventory) {
        return getEmptySlot(inventory, 0, inventory.getSize());
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

    public boolean isWithin(String input, String input1, String input2) {
        double num = NumberUtils.toDouble(input);
        double low = NumberUtils.toDouble(input1);
        double high = NumberUtils.toDouble(input2);
        if (low > high) {
            double temp = high;
            high = low;
            low = temp;
        }
        return num > low && num < high;
    }

    public boolean isWithinOrEqual(String input, String input1, String input2) {
        double num = NumberUtils.toDouble(input);
        double low = NumberUtils.toDouble(input1);
        double high = NumberUtils.toDouble(input2);
        if (low > high) {
            double temp = high;
            high = low;
            low = temp;
        }
        return num >= low && num <= high;
    }

    public boolean isSmaller(String input1, String input2) {
        return NumberUtils.toDouble(input1) < NumberUtils.toDouble(input2);
    }

    public boolean isGreater(String input1, String input2) {
        return NumberUtils.toDouble(input1) > NumberUtils.toDouble(input2);
    }

    public boolean isSmallerOrEqual(String input1, String input2) {
        return NumberUtils.toDouble(input1) <= NumberUtils.toDouble(input2);
    }

    public boolean isGreaterOrEqual(String input1, String input2) {
        return NumberUtils.toDouble(input1) >= NumberUtils.toDouble(input2);
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

    /*
    FOR TRMENU PLUGIN
     */

    private static List<Character> keys = Arrays.asList('#', '-', '+', '|', '=', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');

    public static List<Character> getKeys() {
        return keys;
    }

    private int getEmptySlot(Inventory inventory, int from, int to) {
        for (int i = from; i < to; i++) {
            if (Items.isNull(inventory.getItem(i))) {
                return i;
            }
        }
        return 0;
    }

    public static <T> List<T> castList(Object object, Class<T> classz) {
        List<T> result = new ArrayList<>();
        if (object instanceof List<?>) {
            for (Object o : (List<?>) object) {
                try {
                    result.add(classz.cast(o));
                } catch (Throwable e) {
                    result.add(classz.cast(String.valueOf(o)));
                }
            }
        } else if (object != null) {
            try {
                result.add(classz.cast(object));
            } catch (Throwable e) {
                result.add(classz.cast(String.valueOf(object)));
            }
        }
        return result;
    }


}

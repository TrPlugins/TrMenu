package me.arasple.mc.trmenu.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.tellraw.TellrawJson;
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

    public static <T> List<List<T>> readList(Object object, Class<T> classz) {
        List<List<T>> list = Lists.newArrayList();
        if (object == null) {
            return list;
        } else if (!(object instanceof List)) {
            list.add(Arrays.asList(classz.cast(object)));
        } else if (((List) object).size() <= 0) {
            return list;
        } else if (((List) object).get(0) instanceof List) {
            ((List) object).forEach(x -> list.add((ArrayList<T>) x));
        } else {
            list.add(castList(object, classz));
        }
        return list;
    }

    /**
     * 以玩家身份执行一个(或多个)动作
     *
     * @param player  玩家
     * @param actions 动作
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

    /**
     * 调用 PlaceholderAPI 替换 {} 花括号的变量
     * 如 {player_name}, 而不会替换 %player_name%
     *
     * @param player
     * @param string
     * @return
     */
    public String parseBracketPlaceholders(Player player, String string) {
        return PlaceholderAPI.setBracketPlaceholders(player, string);
    }

    /**
     * 传送玩家到一个 Bungee 服务器中
     *
     * @param player 玩家
     * @param server 服务器名称
     */
    public void connect(Player player, String server) {
        Bungees.connect(player, server);
    }

    /*
    PLAYERS
     */

    /**
     * 发送数据到 Bungee 频道
     *
     * @param player 玩家
     * @param data   数据
     */
    public void sendBungeeData(Player player, String... data) {
        Bungees.sendBungeeData(player, data);
    }

    /**
     * 取得玩家当前菜单传递的参数
     *
     * @param player 玩家名称
     * @return 参数数组
     */
    public String[] getPlayerArgs(String player) {
        return ArgsCache.getPlayerArgs(getPlayer(player));
    }

    /**
     * 判断一个玩家是否在线
     *
     * @param player 玩家名称
     * @return 是否在线
     */
    public boolean isPlayerOnline(String player) {
        return Bukkit.getPlayerExact(player) != null && Bukkit.getPlayerExact(player).isOnline();
    }

    /**
     * 通过名称得到一个玩家的实例
     *
     * @param player 玩家名称
     * @return 玩家实例
     */
    public Player getPlayer(String player) {
        return Bukkit.getPlayerExact(player);
    }

    /*
    TABOOLIB
     */

    /**
     * 取得随机一个在线玩家
     *
     * @return
     */
    public Player getRandomPlayer() {
        return Bukkit.getOnlinePlayers().size() > 0 ? new ArrayList<>(Bukkit.getOnlinePlayers()).get(randomInteger(0, Bukkit.getOnlinePlayers().size() - 1)) : null;
    }

    /**
     * 取得一个 ItemStack 构造器 (from TabooLib5)
     *
     * @return
     */
    public ItemBuilder getItemBuildr() {
        return new ItemBuilder(Material.STONE);
    }


    /*
    数值判断
     */

    /**
     * 取得一个 TellrawJson 构造器 (from TabooLib5)
     *
     * @return
     */
    public TellrawJson getTellraw() {
        return TellrawJson.create();
    }

    /**
     * 取得一个随机整数
     *
     * @param low  最低值
     * @param high 最高值
     * @return 随机整数
     */
    public int randomInteger(int low, int high) {
        int l = Math.min(low, high);
        int h = Math.max(low, high);
        return Numbers.getRandomInteger(l, h);
    }

    /**
     * 取一个随机小数
     *
     * @param low  最低值
     * @param high 最高值
     * @return 随机小数
     */
    public double randomDouble(double low, double high) {
        return Numbers.getRandomDouble(low, high);
    }

    /**
     * 判断一个字符串是否为数值
     *
     * @param number 字符串
     * @return 是否是数值
     */
    public boolean isNumber(String number) {
        return NumberUtils.isParsable(number);
    }

    /**
     * 判断一个数值是否在两个开区间内
     *
     * @param input  待判断的
     * @param input1 端点
     * @param input2 端点
     * @return 是否在开区间内
     */
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

    /**
     * 判断一个数值是否在两个闭区间内
     *
     * @param input  待判断的
     * @param input1 端点
     * @param input2 端点
     * @return 是否在闭区间内
     */
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

    /**
     * 判断一个数值是否小于另一个
     *
     * @param input1 数值
     * @param input2 用来比较的
     * @return input1 < input2
     */
    public boolean isSmaller(String input1, String input2) {
        return NumberUtils.toDouble(input1) < NumberUtils.toDouble(input2);
    }

    /**
     * 判断一个数值是否小于等于另一个
     *
     * @param input1 数值
     * @param input2 用来比较的
     * @return input1 <= input2
     */
    public boolean isSmallerOrEqual(String input1, String input2) {
        return NumberUtils.toDouble(input1) <= NumberUtils.toDouble(input2);
    }

    /**
     * 判断一个数值是否大于另一个
     *
     * @param input1 数值
     * @param input2 用来比较的
     * @return input1 > input2
     */
    public boolean isGreater(String input1, String input2) {
        return NumberUtils.toDouble(input1) > NumberUtils.toDouble(input2);
    }

    /**
     * 判断一个数值是否大于等于另一个
     *
     * @param input1 数值
     * @param input2 用来比较的
     * @return input1 >= input2
     */
    public boolean isGreaterOrEqual(String input1, String input2) {
        return NumberUtils.toDouble(input1) >= NumberUtils.toDouble(input2);
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
    TrMenu 使用
     */

    private static List<Character> keys = Arrays.asList('#', '-', '+', '|', '=', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');

    public static List<Character> getKeys() {
        return keys;
    }

    /**
     * 概率随机判断
     *
     * @param rate 概率值
     * @return 概率返回真
     */
    public boolean chance(double rate) {
        return Numbers.random(rate <= 1 ? rate : rate / 100);
    }

    public int getEmptySlot(Inventory inventory) {
        return getEmptySlot(inventory, 0, inventory.getSize());
    }

    private int getEmptySlot(Inventory inventory, int from, int to) {
        for (int i = from; i < to; i++) {
            if (Items.isNull(inventory.getItem(i))) {
                return i;
            }
        }
        return -1;
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

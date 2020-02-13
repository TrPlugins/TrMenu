package me.arasple.mc.trmenu.hook;

import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.locale.TLocale;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arasple
 * @date 2019/10/6 8:15
 */
@TFunction(enable = "init")
public class HookHeadDatabase {

    private static boolean hoooked;
    private static HeadDatabaseAPI hdb;

    public static void init() {
        hoooked = Bukkit.getPluginManager().getPlugin("HeadDatabase") != null && Bukkit.getPluginManager().getPlugin("HeadDatabase").isEnabled();
        if (isHoooked()) {
            hdb = new HeadDatabaseAPI();
            TLocale.sendToConsole("PLUGIN.HOOKED", "HeadDatabase");
        }
    }

    public static ItemStack getItem(String id) {
        return isHoooked() && hdb != null ? "random".equalsIgnoreCase(id) ? getRandom() : hdb.getItemHead(id) : null;
    }

    public static String getId(ItemStack item) {
        return isHoooked() && hdb != null ? hdb.getItemID(item) : null;
    }

    public static ItemStack getRandom() {
        return isHoooked() && hdb != null ? hdb.getRandomHead() : null;
    }

    public static boolean isHoooked() {
        return hoooked;
    }

}

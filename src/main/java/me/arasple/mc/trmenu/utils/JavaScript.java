package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Scripts;
import me.arasple.mc.trmenu.data.ArgsCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.script.SimpleBindings;
import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/10/5 13:57
 */

public class JavaScript {

    private static SimpleBindings bindings = new SimpleBindings();

    static {
        bindings.put("bukkitServer", Bukkit.getServer());
        bindings.put("TrUtils", TrUtils.getInst());
        bindings.put("Skulls", Skulls.getInst());
    }

    public static Object run(Player player, String script) {
        return run(player, script, null);
    }

    public static Object run(Player player, String script, InventoryClickEvent event) {
        script = Vars.replace(player, script);

        if (Strings.isEmpty(script) || "null".equalsIgnoreCase(script)) {
            return true;
        } else if (script.matches("true|false")) {
            return Boolean.parseBoolean(script);
        } else if (script.matches("(?i)no|yes")) {
            return !"no".equalsIgnoreCase(script);
        }

        if (event == null) {
            event = ArgsCache.getEvent().get(player.getUniqueId());
        }

        bindings.put("player", player);
        if (event != null) {
            if (event instanceof InventoryClickEvent) {
                bindings.put("clickEvent", event);
                bindings.put("clickType", event.getClick());
                bindings.put("clickItemStack", event.getClickedInventory().getItem(event.getRawSlot()));
            }
        }

        try {
            return Scripts.compile(script).eval(bindings);
        } catch (Throwable e) {
            TLocale.sendTo(player, "ERROR.JS", script, e.getMessage(), Arrays.toString(e.getStackTrace()));
            TLocale.sendToConsole("ERROR.JS", script, e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public static SimpleBindings getBindings() {
        return bindings;
    }

}

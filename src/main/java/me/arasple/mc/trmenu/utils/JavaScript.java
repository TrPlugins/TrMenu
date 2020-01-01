package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Scripts;
import me.arasple.mc.trmenu.data.ArgsCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/10/5 13:57
 */
public class JavaScript {

    public static Object run(Player player, String script) {
        return run(player, script, null);
    }

    public static Object run(Player player, String script, InventoryClickEvent event) {
        if (Strings.isEmpty(script) || "null".equalsIgnoreCase(script)) {
            return true;
        }
        event = event != null ? event : ArgsCache.getEvent().get(player.getUniqueId());
        Map<String, Object> bind = new HashMap<>();
        bind.put("player", player);
        bind.put("bukkitServer", Bukkit.getServer());
        if (event != null) {
            if (event instanceof InventoryClickEvent) {
                InventoryClickEvent clickEvent = event;
                bind.put("clickEvent", clickEvent);
                bind.put("clickType", clickEvent.getClick());

                ItemStack itemStack = clickEvent.getClickedInventory().getItem(clickEvent.getRawSlot());
                if (itemStack != null) {
                    bind.put("clickItemStack", itemStack);
                }
            }
        }
        script = Vars.replace(player, script);
        if (script.matches("true|false")) {
            return Boolean.parseBoolean(script);
        } else if (script.matches("no|yes")) {
            return !"no".equalsIgnoreCase(script);
        }
        try {
            return Scripts.compile(script).eval(new SimpleBindings(bind));
        } catch (ScriptException e) {
            TLocale.sendTo(player, "ERROR.JS", script, e.getMessage(), Arrays.toString(e.getStackTrace()));
            TLocale.sendToConsole("ERROR.JS", script, e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

}

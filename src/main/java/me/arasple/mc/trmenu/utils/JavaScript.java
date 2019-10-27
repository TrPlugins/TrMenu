package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.Scripts;
import me.arasple.mc.trmenu.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
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

    public static Object run(Player player, String script, InventoryEvent event) {
        Metrics.increase(1);

        Map<String, Object> bind = new HashMap<>();
        bind.put("player", player);
        bind.put("bukkitServer", Bukkit.getServer());
        if (event != null) {
            if (event instanceof InventoryClickEvent) {
                InventoryClickEvent clickEvent = (InventoryClickEvent) event;

                bind.put("clickEvent", clickEvent);
                bind.put("clickType", clickEvent.getClick());

                ItemStack itemStack = clickEvent.getClickedInventory().getItem(clickEvent.getRawSlot());
                if (itemStack != null) {
                    bind.put("clickItemStack", itemStack);
                }
            }
        }

        script = Vars.replace(player, script);

        try {
            return Scripts.compile(script).eval(new SimpleBindings(bind));
        } catch (ScriptException e) {
            player.closeInventory();
            TLocale.sendTo(player, "ERROR.JS", e.getMessage(), Arrays.toString(e.getStackTrace()));
            TLocale.sendToConsole("ERROR.JS", e.getMessage(), Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

}

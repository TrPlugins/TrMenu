package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.action.TrAction;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.menu.MenuHolder;
import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Arasple
 * @date 2019/10/4 14:02
 */
@TListener
public class ListenerMenuClose implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof MenuHolder)) {
            return;
        }

        Player p = (Player) e.getPlayer();
        Menu menu = ((MenuHolder) e.getInventory().getHolder()).getMenu();

        if (!Strings.isBlank(menu.getCloseRequirement()) && !Boolean.parseBoolean(String.valueOf(JavaScript.run(p, menu.getCloseRequirement())))) {
            TrAction.runActions(menu.getCloseActions().listIterator(), p);
            return;
        }
        if (menu.getCloseActions() != null) {
            menu.getCloseActions().forEach(action -> action.run(p));
        }
        if (ArgsCache.getHeldSlot().containsKey(p.getUniqueId())) {
            p.getInventory().setHeldItemSlot(ArgsCache.getHeldSlot().get(p.getUniqueId()));
            ArgsCache.getHeldSlot().remove(p.getUniqueId());
        }
        ArgsCache.getArgs().remove(p.getUniqueId());
        menu.getButtons().keySet().forEach(b -> {
            b.getDefIcon().getItem().resetIndex(p);
            b.getIcons().values().forEach(i -> i.getItem().resetIndex(p));
        });
    }

}

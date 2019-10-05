package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.inv.Menur;
import me.arasple.mc.trmenu.inv.MenurHolder;
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
    public void onClick(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof MenurHolder)) {
            return;
        }

        Player p = (Player) e.getPlayer();
        Menur menu = ((MenurHolder) e.getInventory().getHolder()).getMenu();

        if (menu.getCloseActions() != null) {
            menu.getCloseActions().forEach(action -> action.onExecute(p, e, ArgsCache.getPlayerArgs(p)));
        }

        ArgsCache.getArgs().remove(p.getUniqueId());
    }

}

package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.actions.ActionRunner;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.menu.Menur;
import me.arasple.mc.trmenu.menu.MenurHolder;
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
        if (!(e.getInventory().getHolder() instanceof MenurHolder)) {
            return;
        }

        Player p = (Player) e.getPlayer();
        Menur menu = ((MenurHolder) e.getInventory().getHolder()).getMenu();

        if (!Strings.isBlank(menu.getCloseRequirement()) && !(boolean) JavaScript.run(p, menu.getCloseRequirement())) {
            ActionRunner.runActions(menu.getCloseActions(), p, null);
            return;
        }

        if (menu.getCloseActions() != null) {
            menu.getCloseActions().forEach(action -> action.onExecute(p, e));
        }

        ArgsCache.getArgs().remove(p.getUniqueId());
    }

}

package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.menu.MenuHolder;
import me.arasple.mc.trmenu.utils.Notifys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 14:02
 */
@TListener
public class ListenerMenuAction implements Listener {

    private HashMap<UUID, Long> clickTimes = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        Notifys.debug(p, "Clicked raw slot §f{0}", e.getRawSlot());

        long start = System.currentTimeMillis();

        if (e.isCancelled() && TrMenu.SETTINGS.getBoolean("OPTIONS.IGNORE-CANCELLED")) {
            Notifys.debug(p, "Ignored cancelled");
            return;
        }
        if (!(e.getInventory().getHolder() instanceof MenuHolder)) {
            Notifys.debug(p, "Not a MenuHolder");
            return;
        }

        Menu menu = ((MenuHolder) e.getInventory().getHolder()).getMenu();
        Button button = menu.getButton(p, e.getRawSlot());

        // Anti ClickSpam
        clickTimes.putIfAbsent(p.getUniqueId(), 0L);
        if (System.currentTimeMillis() - clickTimes.get(p.getUniqueId()) < TrMenu.SETTINGS.getLong("OPTIONS.ANTI-CLICK-SPAM")) {
            e.setCancelled(true);
            Notifys.debug(p, "Anti-Spam, event cancelled.");
            return;
        } else {
            clickTimes.put(p.getUniqueId(), System.currentTimeMillis());
        }
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getAction() == InventoryAction.HOTBAR_SWAP) {
            e.setCancelled(true);
        }
        // Lock PLayer's Inventory
        if (button == null) {
            if (e.getClickedInventory() == p.getInventory() && menu.isLockPlayerInv()) {
                e.setCancelled(true);
            }
            Notifys.debug(p, "Null button");
            return;
        } else {
            e.setCancelled(true);
        }

        button.getIcon(p).onClick(p, button, e.getClick(), e);
        Notifys.debug(p, "§6InventoryClickEvent Took §e{0}ms§6.", System.currentTimeMillis() - start);
    }

}

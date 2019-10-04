package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.inv.Menu;
import me.arasple.mc.trmenu.inv.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 14:02
 */
@TListener
public class ListenerMenuAction implements Listener {

    private HashMap<UUID, Long> clickTimes = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof MenuHolder)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        Menu menu = ((MenuHolder) e.getInventory().getHolder()).getMenu();
        Button button = menu.getButton(e.getRawSlot());

        // 防刷屏点击
        clickTimes.putIfAbsent(p.getUniqueId(), 0L);
        if (System.currentTimeMillis() - clickTimes.get(p.getUniqueId()) < TrMenu.getSettings().getLong("OPTIONS.ANTI-CLICK-SPAM")) {
            e.setCancelled(true);
            return;
        } else {
            clickTimes.put(p.getUniqueId(), System.currentTimeMillis());
        }

        if (button == null) {
            return;
        } else {
            e.setCancelled(true);
        }

        // 执行相关动作
        button.getCurrentIcon().getactions().getOrDefault(e.getClick(), new ArrayList<>()).forEach(action -> {
            action.onExecute(p, e, ArgsCache.getPlayerArgs(p));
        });

        // 刷新图标优先级
        button.refreshConditionalIcon(p);
    }

}

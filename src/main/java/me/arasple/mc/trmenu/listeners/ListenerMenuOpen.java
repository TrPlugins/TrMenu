package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.api.events.MenuOpenEvent;
import me.arasple.mc.trmenu.bstats.Metrics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @author Arasple
 * @date 2019/12/31 13:40
 */
@TListener
public class ListenerMenuOpen implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMenuOpen(MenuOpenEvent e) {
        Metrics.increase(0);
    }

}

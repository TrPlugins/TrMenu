package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import pw.yumc.Yum.events.PluginNetworkEvent;

/**
 * @author Arasple
 * @date 2020/1/20 18:21
 */
@TListener(depend = "Yum")
public class ListenerYumNetwork implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNetWork(PluginNetworkEvent e) {
        if (e.getPlugin() != null && e.getPlugin() == TrMenu.getPlugin()) {
            e.setCancelled(false);
        }
    }

}

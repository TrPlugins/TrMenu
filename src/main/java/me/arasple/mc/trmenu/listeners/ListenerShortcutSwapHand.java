package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * @author Arasple
 * @date 2020/1/28 16:48
 */
@TListener(version = ">=10900")
public class ListenerShortcutSwapHand implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void offHand(PlayerSwapHandItemsEvent e) {
        boolean opened = TrMenuAPI.openByShortcut(e.getPlayer(), (e.getPlayer().isSneaking() ? TrMenu.getSettings().getString("SHORTCUT-OPEN.SNEAKING-OFFHAND", null) : TrMenu.getSettings().getString("SHORTCUT-OPEN.OFFHAND", null)));
        if (opened) {
            e.setCancelled(true);
        }
    }

}

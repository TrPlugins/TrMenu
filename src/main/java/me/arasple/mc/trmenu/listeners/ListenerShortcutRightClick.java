package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * @author Arasple
 * @date 2020/1/23 18:57
 */
@TListener
public class ListenerShortcutRightClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void rightClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof Player && Bukkit.getPlayer(e.getRightClicked().getUniqueId()) != null && Bukkit.getPlayer(e.getRightClicked().getUniqueId()).isOnline()) {
            e.setCancelled(TrMenuAPI.openByShortcut(p, (p.isSneaking() ? TrMenu.getSettings().getString("SHORTCUT-OPEN.SNEAKING-RIGHT-CLICK-PLAYER", null) : TrMenu.getSettings().getString("SHORTCUT-OPEN.RIGHT-CLICK-PLAYER", null)), e.getRightClicked().getName()));
        }
    }

}

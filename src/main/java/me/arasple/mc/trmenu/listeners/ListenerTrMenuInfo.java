package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Arasple
 * @date 2019/10/30 15:00
 */
@TListener
public class ListenerTrMenuInfo implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(react(e.getPlayer(), e.getMessage().startsWith("#") ? e.getMessage().substring(1) : null));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        e.setCancelled(react(e.getPlayer(), e.getMessage().substring(1)));
    }

    private boolean react(Player p, String message) {
        if (!Strings.isBlank(message) && ("trmenur".equalsIgnoreCase(message) || "trixeymenu".equalsIgnoreCase(message))) {
            TLocale.Display.sendTitle(p, "§3§lTr§b§lMenu", "§7Designed by §3Arasple", 10, 35, 10);
            TLocale.Display.sendActionBar(p, Strings.replaceWithOrder(
                    "§7Running version §fv{0}§7, §7Total menus §f{1}",
                    TrMenu.getPlugin().getDescription().getVersion(),
                    TrMenu.getMenus().size()
            ));
            new SoundPack("BLOCK_NOTE_BLOCK_PLING-1-2").play(p);
            return true;
        }
        return false;
    }

}

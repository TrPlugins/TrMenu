package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menur;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Arasple
 * @date 2019/10/4 19:57
 */
@TListener
public class ListenerMenuOpenCommands implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String cmd = e.getMessage().split(" ")[0].substring(1);
        String[] args = ArrayUtils.remove(e.getMessage().split(" "), 0);

        Menur menu = TrMenuAPI.getMenuByCommand(cmd);
        if (menu != null) {
            if (menu.isTransferArgs()) {
                if (args.length < menu.getForceTransferArgsAmount()) {
                    TLocale.sendTo(p, "MENU.NOT-ENOUGH-ARGS", menu.getForceTransferArgsAmount());
                    e.setCancelled(true);
                    return;
                }
            } else if (args.length > 0) {
                return;
            }
            menu.open(p, args);
            e.setCancelled(true);
        }
    }

}

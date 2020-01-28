package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
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
        String[] cmd = e.getMessage().substring(1).split(" ");
        if (cmd.length > 0) {
            for (int i = 0; i < cmd.length; i++) {
                String[] read = read(cmd, i);
                String command = read[0];
                String[] args = ArrayUtils.remove(read, 0);
                Menu menu = TrMenuAPI.getMenuByCommand(command);
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
                    break;
                }
            }
        }
    }

    private static String[] read(String[] cmds, int index) {
        String command;
        if (index == 0) {
            command = cmds[index];
        } else {
            StringBuilder cmd = new StringBuilder();
            for (int i = 0; i <= index; i++) {
                cmd.append(cmds[i]).append(" ");
            }
            command = cmd.substring(0, cmd.length() - 1);
        }
        for (int i = 0; i <= index; i++) {
            cmds = ArrayUtils.remove(cmds, 0);
        }
        return ArrayUtils.insert(0, cmds, command);
    }

}

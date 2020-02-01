package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.util.Files;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.File;

/**
 * @author Arasple
 * @date 2020/1/23 19:29
 */
@Deprecated
public class ListenerTabooLibUpdateCommand implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onCommand(ServerCommandEvent e) {
        if ("tlibupdate".equalsIgnoreCase(e.getCommand())) {
            e.setCancelled(true);
            e.getSender().sendMessage("§8[§fTabooLib§8] §cWARNING §7| §4Update TabooLib will force to restart your server. Please confirm this action by type §c/tlibupdateConfirm");
        } else if ("tlibupdateConfirm".equalsIgnoreCase(e.getCommand()) || "tlibupdate confirm".equalsIgnoreCase(e.getCommand())) {
            e.setCommand("tlibupdatebbb");
            update(e.getSender());
        }
    }

    private void update(CommandSender sender) {
        sender.sendMessage("§8[§fTabooLib§8] §7Downloading TabooLib file...");
        Files.downloadFile(Plugin.URL[0][1], new File("libs/TabooLib.jar"));
        sender.sendMessage("§8[§fTabooLib§8] §2Download completed, the server will restart in 3 secs");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        Bukkit.shutdown();
    }

}

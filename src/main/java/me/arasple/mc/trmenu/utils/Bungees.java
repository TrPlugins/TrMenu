package me.arasple.mc.trmenu.utils;

import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.bukkit.Bukkit.getMessenger;

/**
 * @author Arasple
 * @date 2019/8/4 21:23
 */
public class Bungees {

    public static void init() {
        Plugin plugin = TrMenu.getPlugin();
        if (!getMessenger().isOutgoingChannelRegistered(plugin, "BungeeCord")) {
            getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        }
    }

    public static void connect(Player player, String server) {
        sendBungeeData(player, "Connect", server);
    }

    public static void sendBungeeData(Player player, String... args) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        for (String arg : args) {
            try {
                out.writeUTF(arg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.sendPluginMessage(TrMenu.getPlugin(), "BungeeCord", byteArray.toByteArray());
    }

}

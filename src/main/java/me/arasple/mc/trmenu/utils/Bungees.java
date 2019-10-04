package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/10/4 21:10
 */
public class Bungees {

    @TSchedule
    public static void init() {
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(TrMenu.getPlugin(), "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(TrMenu.getPlugin(), "BungeeCord");
        }
    }

    public static void connect(Player player, String server) {
        sendBungeeData(player, "Connect", server);
    }

    private static void sendBungeeData(Player player, String... args) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        for (String arg : args) {
            try {
                out.writeUTF(arg);
            } catch (IOException e) {
                TrMenu.getTLogger().error("sendBungeeData Error: " + e.toString() + "§c" + Arrays.toString(e.getStackTrace()));
            }
        }
        player.sendPluginMessage(TrMenu.getPlugin(), "BungeeCord", byteArray.toByteArray());
    }

}

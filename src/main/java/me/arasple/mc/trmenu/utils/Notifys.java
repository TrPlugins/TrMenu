package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2019/11/30 10:07
 */
public class Notifys {

    public static void notify(CommandSender[] senders, String path, Object... args) {
        for (CommandSender sender : senders) {
            TLocale.sendTo(sender, path, args);
        }
    }

    public static void sendMsg(CommandSender[] senders, String msg, Object... args) {
        for (CommandSender sender : senders) {
            sender.sendMessage(Strings.replaceWithOrder(msg, args));
        }
    }

}

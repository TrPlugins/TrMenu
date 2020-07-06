package me.arasple.mc.trmenu.utils

import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.TrMenu
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 11:54
 */
object Msger {

    private var debug = false

    fun init() {
        debug = TrMenu.SETTINGS.getBoolean("Options.Debug", false)
    }

    fun cancel() {
        TrMenu.SETTINGS.set("Options.Debug", debug)
    }

    /*
    DEBUG Utils
     */

    fun debug(isDebug: Boolean) {
        this.debug = isDebug
    }

    fun debug(node: String, vararg args: Any) = debug(Bukkit.getConsoleSender(), node, *args)

    fun debug(sender: CommandSender, node: String, vararg args: Any) = debug(sender, node, sender is Player, *args)

    fun debug(sender: CommandSender, node: String, actionBar: Boolean, vararg args: Any) {
        if (((sender is ConsoleCommandSender) && debug) || (sender is Player && sender.hasMetadata("TrMenu:Debug"))) {
            val msg = TLocale.asString("DEBUG.$node", *args)
            if (actionBar && sender is Player) TLocale.Display.sendActionBar(sender, msg)
            else sender.sendMessage(msg)
        }
    }

    /*
    PlaceholderAPI Utils
     */

    fun replace(player: Player, string: String): String = replaceWithPlaceholders(player, replaceWithBracketPlaceholders(player, string))

    fun replaceWithPlaceholders(player: Player, string: String): String = PlaceholderAPI.setPlaceholders(player, string)

    fun replaceWithBracketPlaceholders(player: Player, string: String): String = PlaceholderAPI.setBracketPlaceholders(player, string)


}
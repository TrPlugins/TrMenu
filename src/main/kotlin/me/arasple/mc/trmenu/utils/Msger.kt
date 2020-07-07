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

    fun cancel() = TrMenu.SETTINGS.set("Options.Debug", debug)

    /*
    DEBUG Utils
     */

    fun debug(): Boolean {
        this.debug = !this.debug
        return this.debug
    }

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

    fun replace(player: Player, string: String?): String = replaceWithPlaceholders(player, replaceWithBracketPlaceholders(player, string ?: ""))

    fun replace(player: Player, strings: List<String>): List<String> = replaceWithPlaceholders(player, replaceWithBracketPlaceholders(player, strings))

    fun replaceWithPlaceholders(player: Player, string: String): String = PlaceholderAPI.setPlaceholders(player, string)

    fun replaceWithPlaceholders(player: Player, strings: List<String>): List<String> = PlaceholderAPI.setPlaceholders(player, strings)

    fun replaceWithBracketPlaceholders(player: Player, string: String): String = PlaceholderAPI.setBracketPlaceholders(player, string)

    fun replaceWithBracketPlaceholders(player: Player, strings: List<String>): List<String> = PlaceholderAPI.setBracketPlaceholders(player, strings)

    fun containsPlaceholders(string: String?) = PlaceholderAPI.containsPlaceholders(string) || PlaceholderAPI.containsBracketPlaceholders(string)

    fun printErrors(node: String, throwable: Throwable, vararg args: String) {
        TLocale.sendToConsole("ERRORS.$node", *args, throwable.message, throwable.stackTrace.filter { it.toString().contains("me.arasple.mc.trmenu") })
    }

    fun printErrors(node: String, vararg args: String) {
        TLocale.sendToConsole("ERRORS.$node", *args)
    }

}
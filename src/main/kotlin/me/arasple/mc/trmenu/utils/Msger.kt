package me.arasple.mc.trmenu.utils

import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.MetaPlayer.replaceWithArguments
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
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

    @TFunction.Cancel
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

    fun replace(player: Player, string: String?): String {
        return replaceWithPlaceholders(player, replaceWithBracketPlaceholders(player, string ?: ""))
    }

    fun replace(player: Player, strings: List<String>): List<String> {
        return replaceWithPlaceholders(player, replaceWithBracketPlaceholders(player, strings))
    }

    private fun replaceWithPlaceholders(player: Player, string: String): String = HexColor.translate(PlaceholderAPI.setPlaceholders(player as OfflinePlayer, string))

    private fun replaceWithPlaceholders(player: Player, strings: List<String>): List<String> = strings.map { replaceWithPlaceholders(player, it) }

    fun replaceWithBracketPlaceholders(player: Player, string: String): String = PlaceholderAPI.setBracketPlaceholders(player as OfflinePlayer, player.replaceWithArguments(string))

    fun replaceWithBracketPlaceholders(player: Player, strings: List<String>) = strings.map { replaceWithBracketPlaceholders(player, it) }

    @Suppress("DEPRECATION")
    fun containsPlaceholders(string: String?): Boolean = PlaceholderAPI.containsPlaceholders(string) || PlaceholderAPI.containsBracketPlaceholders(string) || (string != null && string.contains("{") && string.contains("}"))

    fun printErrors(node: String, throwable: Throwable, vararg args: String) = TLocale.sendToConsole("ERRORS.$node", *args, throwable.message, throwable.stackTrace.filter { it.toString().contains("me.arasple.mc.trmenu") }.map { it.toString() + "\n" })

    fun printErrors(node: String, vararg args: String) = TLocale.sendToConsole("ERRORS.$node", *args)

}
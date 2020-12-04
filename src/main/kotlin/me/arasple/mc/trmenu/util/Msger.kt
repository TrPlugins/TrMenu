package me.arasple.mc.trmenu.util

import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.replaceWithArguments
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
    private var preColor = true

    @TFunction.Init
    fun init() {
        preColor = TrMenu.SETTINGS.getBoolean("Options.Pre-Color", true)
        debug = TrMenu.SETTINGS.getBoolean("Options.Debug", false)
    }

    @TFunction.Cancel
    fun cancel() {
        TrMenu.SETTINGS.also {
            it.set("Options.Debug", debug)
            it.saveToFile()
        }
    }

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
        return if (preColor) replaceWithPlaceholders(player, player.replaceWithArguments(color(string) ?: ""))
        else color(replaceWithPlaceholders(player, player.replaceWithArguments(string ?: "")))!!
    }

    fun replace(player: Player, strings: List<String>): List<String> {
        return strings.map { replace(player, it) }
    }

    private fun replaceWithPlaceholders(player: Player, string: String): String {
        return PlaceholderAPI.setPlaceholders(player, string)
    }

    private fun replaceWithPlaceholders(player: Player, strings: List<String>): List<String> {
        return strings.map { replaceWithPlaceholders(player, it) }
    }

    fun replaceWithBracketPlaceholders(player: Player, string: String): String {
        return PlaceholderAPI.setBracketPlaceholders(player, player.replaceWithArguments(string))
    }

    fun replaceWithBracketPlaceholders(player: Player, strings: List<String>): List<String> {
        return strings.map { replaceWithBracketPlaceholders(player, it) }
    }

    @Suppress("DEPRECATION")
    fun containsPlaceholders(string: String?): Boolean {
        return PlaceholderAPI.containsPlaceholders(string)
                || PlaceholderAPI.containsBracketPlaceholders(string)
                || (string != null && string.contains("{") && string.contains("}"))
    }

    fun printErrors(node: String, throwable: Throwable, vararg args: String) {
        TLocale.sendToConsole(
            "ERRORS.$node",
            *args,
            throwable.message,
            throwable.stackTrace.filter { it.toString().contains("me.arasple.mc.trmenu") }.map { it.toString() + "\n" })
    }

    fun printErrors(node: String, vararg args: String) = TLocale.sendToConsole("ERRORS.$node", *args)

    fun color(string: String?): String? = if (string.isNullOrBlank()) string else Hex.colorify(string)

}
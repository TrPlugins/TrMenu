package me.arasple.mc.trmenu

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.expression.Expressions
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.PlaceholderAPIPlugin
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2020/2/29 17:27
 */
class TrMenuLoader {

    fun init() {
        if (!TrMenu.SETTINGS.getBoolean("Options.Hide-Logo", false)) {
            printLogo()
        }
        TLocale.sendToConsole("PLUGIN.LOADING", Version.getBukkitVersion())
    }

    fun active() {
        Expressions.reload()
        TLocale.sendToConsole("PLUGIN.LOADED", TrMenu.plugin.description.version)
    }

    fun cancel() {
        // 注销插件提供的 PlaceholderAPI 变量拓展
        PlaceholderAPI.unregisterExpansion(PlaceholderAPIPlugin.getInstance().expansionManager.getRegisteredExpansion("trmenu"))
    }


    /**
     * 打印插件的 Logo 字符画到控制台
     */
    private fun printLogo() = arrayOf(
            "§8___________         _____                     __________",
            "§8\\__    ___/______  /     \\   ____   ____  __ _\\______   \\_______  ____",
            "§8   |    |  \\_  __ \\/  \\ /  \\_/ __ \\ /    \\|  |  \\     ___/\\_  __ \\/  _ \\",
            "§8   |    |   |  | \\/    Y    \\  ___/|   |  \\  |  /    |     |  | \\(  <_> )",
            "§8   |____|   |__|  \\____|__  /\\___  >___|  /____/|____|     |__|   \\____/",
            "§8                       \\/     \\/     \\/                               ",
            "                                                      "
    ).let {
        it.forEachIndexed { index, raw ->
            if (raw.isNotBlank()) {
                val line = raw.toCharArray()
                val width = (3..6).random()
                var randomIndex: Int
                do {
                    randomIndex = (2..line.size - width).random()
                } while (String(line.copyOfRange(randomIndex, randomIndex + width)).isBlank())
                val replace = String(line.copyOfRange(randomIndex, randomIndex + width))
                it[index] = String(line).replaceFirst(replace, "§${arrayOf('9', 'b', '3').random()}$replace§8")
            }
        }
        Bukkit.getConsoleSender().sendMessage(it)
    }


}
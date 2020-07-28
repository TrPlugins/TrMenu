package me.arasple.mc.trmenu

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.configuration.MenuLoader
import me.arasple.mc.trmenu.modules.hook.HookHeadDatabase
import me.arasple.mc.trmenu.modules.hook.HookPlaceholderAPI
import me.arasple.mc.trmenu.modules.hook.HookPlayerPoints
import me.arasple.mc.trmenu.utils.FileWatcher
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
        if (HookPlaceholderAPI.installDepend()) {
            return;
        }
        TLocale.sendToConsole("PLUGIN.LOADING", Version.getBukkitVersion())
    }

    fun active() {
        HookHeadDatabase.init()
        HookPlayerPoints.init()

        TLocale.sendToConsole("PLUGIN.LOADED", TrMenu.plugin.description.version)
        MenuLoader.loadMenus()
    }

    fun cancel() {
        // 注销插件提供的 PlaceholderAPI 变量拓展
        PlaceholderAPI.unregisterExpansion(PlaceholderAPIPlugin.getInstance().expansionManager.getRegisteredExpansion("trmenu"))
        FileWatcher.watcher.unregisterAll()
    }

    private fun printLogo() = arrayOf(
        "§8  ___________         _____                        ________",
        "§8    \\__    ___/______  /     \\   ____   ____  __ __  \\_____  \\",
        "§8    |    |  \\_  __ \\/  \\ /  \\_/ __ \\ /    \\|  |  \\  /  ____/",
        "§8    |    |   |  | \\/    Y    \\  ___/|   |  \\  |  / /       \\",
        "§8    |____|   |__|  \\____|__  /\\___  >___|  /____/  \\_______ \\",
        "§8    \\/     \\/     \\/                \\/",
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
package me.arasple.mc.trmenu

import io.izzel.taboolib.TabooLib
import io.izzel.taboolib.Version
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.locale.TLocaleLoader
import me.arasple.mc.trmenu.commands.registerable.RegisterCommands
import me.arasple.mc.trmenu.modules.configuration.MenuLoader
import me.arasple.mc.trmenu.modules.hook.HookHeadDatabase
import me.arasple.mc.trmenu.modules.hook.HookPlaceholderAPI
import me.arasple.mc.trmenu.modules.hook.HookPlayerPoints
import me.arasple.mc.trmenu.modules.shortcut.Shortcuts
import me.arasple.mc.trmenu.utils.Watchers
import me.clip.placeholderapi.PlaceholderAPIPlugin
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2020/2/29 17:27
 */
class TrMenuLoader {

    fun init() {
        TrMenu.SETTINGS.listener {
            register()
        }
        if (!TrMenu.SETTINGS.getBoolean("Options.Hide-Logo", false)) {
            printLogo()
        }
        if (HookPlaceholderAPI.installDepend()) {
            return
        }
        register()
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
        PlaceholderAPIPlugin.getInstance().localExpansionManager.findExpansionByIdentifier("trmenu").ifPresent {
            it.unregister()
        }
        Watchers.watcher.unregisterAll()
    }

    private fun register() {
        syncLocale()
        RegisterCommands.load()
        Shortcuts.load()
    }

    private fun syncLocale() {
        if (TLocaleLoader.getLocalPriorityFirst(TrMenu.plugin) != "zh_CN" && TLocaleLoader.getLocalPriorityFirst(TabooLib.getPlugin()) == "zh_CN") {
            TabooLib.getConfig().also {
                it.set("LOCALE.PRIORITY", listOf("en_US", "zh_CN"))
                it.saveToFile()
            }
        }
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
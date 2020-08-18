package me.arasple.mc.trmenu.modules.configuration

import io.izzel.taboolib.util.Files
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.modules.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.modules.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.utils.Watchers
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.io.File

/**
 * @author Arasple
 * @date 2020/7/28 11:46
 */
object MenuLoader {

    fun loadMenus() = loadMenus(Bukkit.getConsoleSender())

    fun loadMenus(sender: CommandSender) {
        val start = System.currentTimeMillis()
        val folder = setupMenus()
        grabMenuFiles(folder).forEach {
            loadMenu(it)?.let { m -> Menu.getMenus().add(m) }
        }
        TrMenu.SETTINGS.getStringList("Load-Menu-Files").forEach { it ->
            val file = File(it)
            if (file.exists()) grabMenuFiles(file).forEach { loadMenu(it)?.let { m -> Menu.getMenus().add(m) } }
        }
        sender.sendLocale("LOADER.MENU", Menu.getMenus().size, System.currentTimeMillis() - start)
    }

    fun loadMenu(file: File) = loadMenu(file, true)

    fun loadMenu(file: File, listener: Boolean) =
        MenuSerializer.loadMenu(file.name.removeSuffix(".yml"), MenuConfiguration(file.absolutePath).let {
            it.load(file)
            return@let it
        }).let { menu ->
            if (listener && menu != null && file.exists() && !Watchers.isListening(file)) {
                val id = menu.id
                Watchers.listener(file) {
                    Tasks.task(true) {
                        TrMenuAPI.getMenuById(id)?.reload()
                    }
                }
            }
            menu
        }

    fun grabMenuFiles(file: File): List<File> =
        mutableListOf<File>().let { files ->
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    files.addAll(grabMenuFiles(it))
                }
            } else if (!file.name.startsWith("#") && file.name.endsWith(".yml", true)) {
                files.add(file)
            }
            return@let files
        }

    private fun setupMenus(): File {
        Menu.clearMenus()
        val folder = File(TrMenu.plugin.dataFolder, "menus")
        if (!folder.exists()) {
            Files.releaseResource(TrMenu.plugin, "menus/Example.yml", true)
        }

        return folder
    }

}
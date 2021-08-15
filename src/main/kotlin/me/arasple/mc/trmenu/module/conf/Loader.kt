package me.arasple.mc.trmenu.module.conf

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.util.file.FileListener
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.function.console
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang
import java.io.File
import kotlin.system.measureNanoTime

/**
 * @author Arasple
 * @date 2021/1/28 19:56
 */
object Loader {

    private val folder by lazy {
        Menu.menus.clear()
        val folder = File(TrMenu.plugin.dataFolder, "menus")

        if (!folder.exists()) {
            arrayOf(
                "Example.yml",
                "Demo-Buttons.yml",
                "Profile.yml",
                "shop-example/Shop-Categories.yml",
                "shop-example/categories/Shop-Ores.yml",
                "shop-example/handler/Shop-Handler-Purchase.yml",
                "shop-example/handler/Shop-Handler-Sell.yml",
            ).forEach { releaseResourceFile("menus/$it", true) }
        }

        folder
    }

    /**
     * 载入默认路径 & 自定义路径的所有菜单
     */
    fun loadMenus(sender: CommandSender = Bukkit.getConsoleSender()) {
        Menu.menus.removeIf { it ->
            it.forSessions { it.close(true, updateInventory = true) }
            true
        }
        measureNanoTime {

            val errors = mutableListOf<String>()
            val func: (List<File>) -> Unit = { list ->
                list.forEach {
                    val result = MenuSerializer.serializeMenu(it)
                    if (result.succeed()) {
                        val menu = result.result as Menu
                        Menu.menus.add(menu)
                        listen(it)
                    } else errors.addAll(result.errors)
                }
            }

            func(filterMenuFiles(folder))
            func(TrMenu.SETTINGS.getStringList("Loader.Menu-Files").flatMap { filterMenuFiles(File(it)) })

        }.also {
            sender.sendLang("Menu-Loaded", Menu.menus.size, it / 1000000.0)
        }

    }

    /**
     * 监听菜单
     */
    private fun listen(file: File) {
        if (!FileListener.isListening(file)) {
            FileListener.listener(file) {
                val reload = MenuSerializer.serializeMenu(file)
                val current = TrMenuAPI.getMenuById(file.nameWithoutExtension) ?: return@listener

                if (reload.succeed()) {
                    val reloadMenu = reload.result as Menu

                    current.forSessions { s ->
                        reloadMenu.open(s.viewer, s.page, MenuOpenEvent.Reason.RELOAD)
                    }

                    Menu.menus.replaceAll { target ->
                        if (target == current) {
                            target.removeViewers()
                            reloadMenu
                        } else target
                    }

                    console().sendLang("Menu-Reloaded", file.name)
                }
            }
        }
        FileListener.clear()
    }


    /**
     * 过滤有效菜单文件
     */
    private fun filterMenuFiles(file: File): List<File> {
        return mutableListOf<File>().run {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    addAll(filterMenuFiles(it))
                }
            } else if (!file.name.startsWith("#") && file.extension.equals("yml", true)) {
                add(file)
            }
            this
        }
    }

}
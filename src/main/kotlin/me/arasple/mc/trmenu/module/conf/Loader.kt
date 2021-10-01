package me.arasple.mc.trmenu.module.conf

import me.arasple.mc.trmenu.util.concurrent.TaskConcurrent
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.conf.prop.SerialzeResult
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.util.file.FileListener
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.function.console
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang
import java.io.File

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
     * 多线程载入默认路径 & 自定义路径的所有菜单
     */
    fun loadMenus(sender: CommandSender = Bukkit.getConsoleSender(), async: Boolean = false) {
        if (!async) Menu.menus.removeIf { it ->
            it.forSessions { it.close(true, updateInventory = true) }
            true
        }

        val errors = mutableListOf<String>()
        val tasks = mutableListOf<File>().also {
            it.addAll(filterMenuFiles(folder))
            it.addAll(TrMenu.SETTINGS.getStringList("Loader.Menu-Files").flatMap { filterMenuFiles(File(it)) })
        }
        val taskConcurrent = TaskConcurrent<File, SerialzeResult>(tasks) { it / 2 }

        val serializingTime = System.currentTimeMillis()

        taskConcurrent.start(
            // serializing
            {
                // 修复 Purpur 的空文件载入问题
                if (it.readText().isBlank()) {
                    return@start SerialzeResult(SerialzeResult.Type.MENU, SerialzeResult.State.FAILED)
                }

                val result = MenuSerializer.serializeMenu(it)
                if (result.succeed()) {
                    listen(it)
                } else errors.addAll(result.errors)
                result
            },
            // success
            {
                synchronized(Menu.menus) {
                    if (async) Menu.menus.removeIf { it ->
                        it.forSessions { it.close(true, updateInventory = true) }
                        true
                    }
                    it.forEach {
                        val menu = (it.result as? Menu) ?: return@forEach
                        Menu.menus.add(menu)
                    }
                }
                sender.sendLang("Menu-Loader-Loaded", Menu.menus.size, System.currentTimeMillis() - serializingTime)
            }
        ).get()
    }

    /**
     * 监听菜单
     */
    private fun listen(file: File) {
        if (!FileListener.isListening(file)) {
            FileListener.listener(file) {
                val start = System.currentTimeMillis()
                val reload = try {
                    MenuSerializer.serializeMenu(file).also {
                        if (!it.succeed()) {
                            console().sendLang(
                                "Menu-Loader-Failed",
                                file.nameWithoutExtension,
                                it.type.name
                            )
                            it.errors.forEach { console().sendMessage("    §8$it") }
                        }
                    }
                } catch (t: Throwable) {
                    console().sendLang(
                        "Menu-Loader-Failed",
                        file.nameWithoutExtension,
                        t.message ?: "Nothing..."
                    )
                    console().sendMessage("§8${t.stackTraceToString()}")
                    return@listener
                }
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

                    console().sendLang("Menu-Loader-Reloaded", file.name, System.currentTimeMillis() - start)
                }
            }
        }
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
package trplugins.menu.module.conf

import trplugins.menu.util.concurrent.TaskConcurrent
import trplugins.menu.TrMenu
import trplugins.menu.api.TrMenuAPI
import trplugins.menu.api.event.MenuOpenEvent
import trplugins.menu.module.conf.prop.SerialzeResult
import trplugins.menu.module.display.Menu
import trplugins.menu.util.file.FileListener
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

    var isLoading = false
        private set

    /**
     * 多线程载入默认路径 & 自定义路径的所有菜单
     */
    fun loadMenus(sender: CommandSender = Bukkit.getConsoleSender(), async: Boolean = false) {
        isLoading = true
        if (!async) Menu.menus.removeIf { it ->
            it.forSessions { it.close(true, updateInventory = true) }
            true
        }

        val errors = mutableListOf<String>()

        val files = mutableListOf<File>().also {
            it.addAll(filterMenuFiles(folder))
            it.addAll(TrMenu.SETTINGS.getStringList("Loader.Menu-Files").flatMap { filterMenuFiles(File(it)) })
        }
        val tasks = mutableListOf<File>().also { tasks ->
            files.forEach { file ->
                if (!tasks.any { it.nameWithoutExtension == file.nameWithoutExtension })
                    tasks.add(file)
            }
        }

        val taskConcurrent = TaskConcurrent<File, SerialzeResult>(
            tasks,
            if (TrMenu.SETTINGS.getBoolean("Options.Multi-Thread", true)) TrMenu.performance.pools else 1
        )

        val serializingTime = System.currentTimeMillis()

        taskConcurrent.start(
            // serializing
            {
                val result = MenuSerializer.serializeMenu(it)
                if (result.state == SerialzeResult.State.IGNORE) {
                    return@start result
                }
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
                    it.forEach { result ->
                        if (result.second.state == SerialzeResult.State.IGNORE) {
                            return@forEach
                        }
                        val menu = (result.second.result as? Menu) ?: return@forEach let {
                            if (result.first.isDirectory) {
                                return@let
                            }
                            console().sendLang(
                                "Menu-Loader-Failed",
                                result.first.nameWithoutExtension,
                                result.second.type.name
                            )
                            result.second.errors.forEach { console().sendMessage("    §8$it") }
                            console().sendMessage("")
                        }
                        Menu.menus.add(menu)
                    }
                }
                sender.sendLang("Menu-Loader-Loaded", Menu.menus.size, System.currentTimeMillis() - serializingTime)
                isLoading = false
            }
        ).get()
    }

    /**
     * 监听菜单
     */
    internal fun listen(file: File) {
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
                            console().sendMessage("")
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
            } else if (file.exists() && !file.name.startsWith("#")) {
                add(file)
            }
            this
        }
    }

}
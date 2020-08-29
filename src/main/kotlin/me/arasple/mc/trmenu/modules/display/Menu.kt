package me.arasple.mc.trmenu.modules.display

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.completeArguments
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.Extends.setMenuSession
import me.arasple.mc.trmenu.api.event.MenuCloseEvent
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.api.nms.NMS
import me.arasple.mc.trmenu.modules.conf.MenuLoader
import me.arasple.mc.trmenu.modules.conf.menu.MenuConfiguration
import me.arasple.mc.trmenu.modules.data.Sessions
import me.arasple.mc.trmenu.modules.display.menu.MenuLayout
import me.arasple.mc.trmenu.modules.display.menu.MenuSettings
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
import me.arasple.mc.trmenu.util.Msger
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.Tasks.Tasking
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.math.min
import kotlin.system.measureTimeMillis

/**
 * @author Arasple
 * @date 2020/5/30 13:24
 */
class Menu(val id: String, val conf: MenuConfiguration, val settings: MenuSettings, val layout: MenuLayout, val icons: Set<Icon>, val viewers: MutableSet<Player>, val tasking: Tasking) {

    constructor(id: String, conf: MenuConfiguration, settings: MenuSettings, layout: MenuLayout, icons: Set<Icon>) : this(id, conf, settings, layout, icons, mutableSetOf(), Tasking())

    init {
        layout.locateIcons(icons)
        icons.forEach { icon ->
            icon.settings.fixUpdate(icon)
        }
    }

    fun open(player: Player) = open(player, -1)

    fun open(player: Player, page: Int) = open(player, page, MenuOpenEvent.Reason.UNKNOWN)

    fun open(player: Player, page: Int, reason: MenuOpenEvent.Reason) {
        resetTaskings(player)
        Tasks.task(true) {
            Mirror.eval("Menu:onOpen(total)") {
                val p = (if (page < 0) settings.options.getDefaultLayout(player) else min(page, layout.layouts.size - 1)).coerceAtLeast(0)
                val e = MenuOpenEvent(player, this, p, reason, MenuOpenEvent.Result.UNKNOWN).async(true).call() as MenuOpenEvent
                val s = player.getMenuSession()

                if (layout.layouts.size <= e.page) {
                    e.result = MenuOpenEvent.Result.ERROR_PAGE
                    e.isCancelled = true
                    return@eval
                }
                if (!e.isCancelled) {
                    player.completeArguments(settings.options.defaultArguments)
                    val l = layout.layouts[p].also { s.set(this, it, p) }

                    Tasks.delay(measureTimeMillis { e.menu.refreshIcons(player, p) } / 50, true) {
                        l.displayInventory(player, settings.title.getTitle(player))
                        icons.filter { it.isInPage(p) }.forEach {
                            it.displayItemStack(player)
                            it.startUpdateTasks(player, this)
                        }
                    }

                    settings.load(player, this, l)
                    viewers.add(player)
                }
                if (reason == MenuOpenEvent.Reason.SWITCH_PAGE) {
                    NMS.sendClearNonIconSlots(player, s)
                }
            }
        }
    }

    fun close(closeInventory: Boolean) {
        viewers.removeIf {
            tasking.reset(it)
            MenuCloseEvent(it, this@Menu, -1, MenuCloseEvent.Reason.CONSOLE, true).call()
            if (closeInventory) it.closeInventory()

            true
        }
    }

    /**
     * 为特定玩家关闭此菜单
     */

    fun close(player: Player, page: Int) {
        close(player, page, MenuCloseEvent.Reason.CONSOLE, closeInventory = false, silent = true)
    }

    fun close(player: Player, page: Int, reason: MenuCloseEvent.Reason, closeInventory: Boolean, silent: Boolean) {
        tasking.reset(player)

        Tasks.task {
            MenuCloseEvent(player, this@Menu, page, reason, silent).call()
            layout.layouts[page].close(player, closeInventory)
            player.setMenuSession(null, null, -1)
            if (closeInventory) player.closeInventory() else player.updateInventory()
        }

        // 防止关闭菜单后, 动态标题周期未停止
        Tasks.delay(3, true) {
            if (player.getMenuSession().isNull()) {
                NMS.sendCloseWindow(player)
            }
        }
    }

    fun refreshIcons(player: Player, page: Int) = icons.filter { it.isInPage(page) && it.subIcons.isNotEmpty() }.forEach { it.refreshIcon(player) }

    fun resetIcons(player: Player, session: Session) = icons.forEach { it.setItemStack(player, session) }

    fun getOccupiedSlots(player: Player, page: Int) = mutableSetOf<Int>().let { slots ->
        icons.forEach {
            val find = it.getIconProperty(player).display.position[page]?.currentElement(player)?.getOccupiedSlots(player)
            if (find != null) {
                slots.addAll(find)
            }
        }
        return@let slots
    }

    fun getEmptySlots(player: Player, page: Int) = IntRange(0, layout.layouts[page].size() + 35).toMutableSet().let {
        it.removeAll(getOccupiedSlots(player, page))
        it
    }

    fun getIcon(player: Player, page: Int, slot: Int): Icon? = getIcons(player, page).firstOrNull {
        it.getIconProperty(player).display.position[page]?.currentElement(player)?.getSlots(player)?.contains(slot) ?: false
    }

    fun getIcons(player: Player, page: Int) = icons.filter { it.getIconProperty(player).display.position.containsKey(page) }

    fun reload() {
        Tasks.task(true) {
            val file = File(conf.loadedPath)
            if (file.exists()) {
                val menu = MenuLoader.loadMenu(file, false) ?: return@task
                var delay = 0L

                getMenus().add(menu)
                getMenus().remove(this)

                viewers.forEach {
                    Tasks.delay(delay, true) {
                        val session = it.getMenuSession()
                        menu.open(it, session.page, MenuOpenEvent.Reason.RELOAD)
                    }
                    delay += 2
                }
            }
        }
    }

    class Session(var menu: Menu?, var layout: MenuLayout.Layout?, var page: Int, var fromLayout: MenuLayout.Layout?, var id: Int = 0) {

        constructor() : this(null, null, 0, null)

        fun isNull(): Boolean {
            return menu == null || layout == null
        }

        fun set(menu: Menu?, layout: MenuLayout.Layout?, page: Int) {
            Msger.debug("SESSION", Sessions.getPlayer(this)?.name ?: "null", menu?.id ?: "null", page)

            this.menu = menu
            this.layout = layout
            this.page = page
            this.id++
        }

        fun safeClose(player: Player) {
            if (!isNull()) {
                menu!!.viewers.remove(player)
                set(null, null, -1)
            }
        }

        fun close(player: Player) {
            if (!isNull()) {
                menu!!.close(player, page)
            }
        }

        fun isDifferent(id: Int): Boolean {
            return id != this.id
        }

    }

    companion object {

        private val menus = mutableMapOf<String, MutableList<Menu>>()

        fun getAllMenus(): MutableMap<String, MutableList<Menu>> = menus

        fun getMenus(): MutableList<Menu> = menus.computeIfAbsent(TrMenu.plugin.name) { mutableListOf() }

        fun getMenus(plugin: Plugin): MutableList<Menu>? = menus[plugin.name]

        fun clearMenus() = clearMenus(TrMenu.plugin)

        fun resetTaskings(player: Player) {
            getAllMenus().flatMap { it.value }.forEach {
                it.tasking.reset(player)
            }
        }

        private fun clearMenus(plugin: Plugin) {
            getMenus(plugin)?.clear()
        }

    }

}
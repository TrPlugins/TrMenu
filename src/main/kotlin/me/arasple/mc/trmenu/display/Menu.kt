package me.arasple.mc.trmenu.display

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.events.MenuCloseEvent
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MetaPlayer
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.display.menu.MenuSettings
import me.arasple.mc.trmenu.utils.Tasking
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.math.min

/**
 * @author Arasple
 * @date 2020/5/30 13:24
 */
class Menu(val id: String, val conf: MenuConfiguration, val settings: MenuSettings, val layout: MenuLayout, val icons: Set<Icon>, val viewers: MutableSet<Player>, val tasking: Tasking) {

    constructor(id: String, conf: MenuConfiguration, settings: MenuSettings, layout: MenuLayout, icons: Set<Icon>) : this(id, conf, settings, layout, icons, mutableSetOf(), Tasking())

    init {
        layout.locateIcons(icons)
    }

    /**
     * 为玩家打开此菜单
     */
    fun open(player: Player, page: Int, reason: MenuOpenEvent.Reason) {
        val p = if (page < 0) settings.options.defaultLayout else min(page, layout.layouts.size - 1)
        val e = MenuOpenEvent(player, this, p, reason, MenuOpenEvent.Result.UNKNOWN).call()

        if (layout.layouts.size <= e.page) {
            e.result = MenuOpenEvent.Result.ERROR_PAGE
            e.isCancelled = true
            return
        }
        if (!e.isCancelled) {
            val layout = layout.layouts[p]

            MetaPlayer.completeArguments(player, settings.options.defaultArguments)
            MenuSession.session(player).set(this, layout, p)

            layout.displayInventory(player, settings.title.getTitle(player))
            loadIcons(player, p)
            settings.load(player, this, layout)
            viewers.add(player)
        }
    }

    /**
     * 为所有正在查看此菜单的玩家关闭此菜单
     */
    fun close() {
        Tasks.run {
            viewers.forEach {
                tasking.reset(it)
                MenuCloseEvent(it, this, -1, MenuCloseEvent.Reason.CONSOLE, true).call()
                it.closeInventory()
            }
        }
        viewers.clear()
    }

    /**
     * 为特定玩家关闭此菜单
     */
    fun close(player: Player, page: Int, reason: MenuCloseEvent.Reason, closeInventory: Boolean, silent: Boolean) {
        Tasks.run {
            tasking.reset(player)
            MenuCloseEvent(player, this, page, reason, silent).call()
            layout.layouts[page].close(player, closeInventory)
            MenuSession.session(player).set(null, null, -1)
            if (closeInventory) player.closeInventory() else player.updateInventory()
        }
        viewers.remove(player)
    }

    /**
     * 载入图标
     */
    fun loadIcons(player: Player, page: Int) = icons.filter { it.isInPage(page) }.forEach { it.displayIcon(player, this) }

    fun resetIcons(player: Player) = icons.forEach { it.displayItemStack(player) }

    fun getOccupiedSlots(player: Player, page: Int) = mutableSetOf<Int>().let { slots ->
        icons.forEach {
            val find = it.getIconProperty(player).display.position[page]?.currentElement(player)?.getSlots(player)
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

    fun getIcon(player: Player, page: Int, slot: Int): Icon? = getIcons(player, page).firstOrNull { it.getIconProperty(player).display.position[page]?.currentElement(player)?.getSlots(player)?.contains(slot) ?: false }

    fun getIcons(player: Player, page: Int) = icons.filter { it.getIconProperty(player).display.position.containsKey(page) }

    companion object {

        private val menus = mutableMapOf<String, MutableList<Menu>>()

        fun getAllMenus(): MutableMap<String, MutableList<Menu>> = menus

        fun getMenus(): MutableList<Menu> = menus.computeIfAbsent(TrMenu.plugin.name) { mutableListOf() }

        fun getMenus(plugin: Plugin): MutableList<Menu>? = menus[plugin.name]

        fun clearMenus() = clearMenus(TrMenu.plugin)

        private fun clearMenus(plugin: Plugin) {
            val menus = getMenus(plugin)
            if (menus != null) {
                menus.forEach { it.close() }
                menus.clear()
            }
        }

    }

}
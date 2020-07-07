package me.arasple.mc.trmenu.display

import me.arasple.mc.trmenu.api.events.MenuCloseEvent
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MetaPlayer
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.display.menu.MenuSettings
import me.arasple.mc.trmenu.modules.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 13:24
 */
class Menu(val id: String, val conf: MenuConfiguration, val settings: MenuSettings, val layout: MenuLayout, val icons: Set<Icon>, val viewers: MutableSet<Player>) {

    fun open(player: Player, page: Int, reason: MenuOpenEvent.Reason) {
        val event = MenuOpenEvent(player, this, page, reason, MenuOpenEvent.Result.UNKNOWN).call()

        if (layout.layouts.size <= page) {
            event.result = MenuOpenEvent.Result.ERROR_PAGE
            event.isCancelled = true
            return
        }
        if (!event.isCancelled) {
            val layout = layout.layouts[page]

            MetaPlayer.completeArguments(player, settings.options.defaultArguments)
            MenuSession.session(player).set(this, layout, page)

            layout.displayInventory(player, settings.title.getTitle(player))

            icons.forEach { it.displayIcon(player, this) }
            settings.title.load(player, this, layout)
            settings.options.run(player, layout)
            viewers.add(player)
        }
    }

    fun close() {
        Tasks.runTask(Runnable {
            viewers.forEach {
                MenuCloseEvent(it, this, -1, MenuCloseEvent.Reason.CONSOLE, true).call()
                it.closeInventory()
            }
        })
        viewers.clear()
    }

    fun close(player: Player, page: Int, reason: MenuCloseEvent.Reason, closeInventory: Boolean, silent: Boolean) {
        Tasks.runTask(Runnable {
            MenuCloseEvent(player, this, page, reason, silent).call()
            layout.layouts[page].close(player, closeInventory)
            MenuSession.session(player).set(null, null, -1)
            player.closeInventory()
        })
        viewers.remove(player)
    }

    fun loadIcons(player: Player) = icons.forEach { it.displayIcon(player, this) }

    fun resetIcons(player: Player) = icons.forEach { it.displayItemStack(player) }

    fun getOccupiedSlots(player: Player, page: Int) = mutableListOf<Int>().let { slots ->
        icons.forEach {
            val find = it.getIconProperty(player).display.position[page]?.currentElement(player)?.getSlots(player)
            if (find != null) {
                slots.addAll(find)
            }
        }
        return@let slots
    }

    fun getIcon(player: Player, page: Int, slot: Int): Icon? = getIcons(player, page).firstOrNull { it.getIconProperty(player).display.position[page]?.currentElement(player)?.getSlots(player)?.contains(slot) ?: false }

    fun getIcons(player: Player, page: Int) = icons.filter { it.getIconProperty(player).display.position.containsKey(page) }

}
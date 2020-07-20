package me.arasple.mc.trmenu.api.factory

import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/7/20 20:06
 */
class MenuFactorySession(val player: Player, var menuFactory: MenuFactory?, val def: MutableMap<String, Pair<ItemStack, Set<Int>>>, val items: MutableMap<Int, ItemStack>) {

    fun getItem(slot: Int): Pair<String, ItemStack?>? {
        def.entries.firstOrNull { it.value.second.contains(slot) }?.let {
            val id = it.key
            val item = it.value.first
            return Pair(id, item)
        }
        return Pair("", items[slot])
    }

    fun displayItems() {
        def.values.forEach { it ->
            val item = it.first
            it.second.forEach {
                PacketsHandler.sendOutSlot(player, MenuSession.TRMENU_WINDOW_ID, it, item)
            }
        }
        items.forEach {
            PacketsHandler.sendOutSlot(player, MenuSession.TRMENU_WINDOW_ID, it.key, it.value)
        }
    }

    fun setItem(slot: Int, item: ItemStack, display: Boolean = false) {
        items[slot] = item
        if (display) PacketsHandler.sendOutSlot(player, MenuSession.TRMENU_WINDOW_ID, slot, item)
    }

    fun removeItem(slot: Int, display: Boolean = false) {
        items.remove(slot)
        if (display) PacketsHandler.sendRemoveSlot(player, MenuSession.TRMENU_WINDOW_ID, slot)
    }

    fun display(type: InventoryType, size: Int, title: String) {
        PacketsHandler.sendOpenWindow(player, MenuSession.TRMENU_WINDOW_ID, type, size, title)
    }

    fun isNull() = menuFactory == null

    fun reset() {
        menuFactory = null
        def.clear()
    }

}
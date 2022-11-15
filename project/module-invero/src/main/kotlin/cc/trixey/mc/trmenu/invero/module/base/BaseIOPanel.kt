package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Arasple
 * @since 2022/11/15 18:31
 */
abstract class BaseIOPanel : Panel {

    private val storage = ConcurrentHashMap<Int, ItemStack>()

    override fun handleClick(window: Window, e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    override fun handleItemsCollect(window: Window, e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    override fun handleItemsMove(window: Window, e: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

    operator fun set(index: Int, itemStack: ItemStack) {
        storage[index] = itemStack
    }

    operator fun get(index: Int): ItemStack? {
        return storage[index]
    }

}
package cc.trixey.mc.invero.common.panel

import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.ScaleView
import cc.trixey.mc.invero.common.Window
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Arasple
 * @since 2022/11/15 18:31
 */
abstract class BaseIOPanel(
    scale: ScaleView,
    weight: PanelWeight
) : PanelInstance(scale, weight) {

    val storage = ConcurrentHashMap<Int, ItemStack>()

    fun updateStorage(window: Window) {
        // TODO 需要改进的逻辑

        for (key in storage.keys) {
            val slot = key.toUpperSlot() ?: continue
            storage[key] = window.inventorySet[slot] ?: ItemStack(Material.AIR)
        }
    }

    operator fun set(index: Int, itemStack: ItemStack) {
        storage[index] = itemStack
    }

    operator fun get(index: Int): ItemStack? {
        return storage[index]
    }

}
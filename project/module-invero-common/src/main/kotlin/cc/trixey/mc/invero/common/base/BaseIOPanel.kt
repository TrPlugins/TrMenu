package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.PanelScale
import cc.trixey.mc.invero.common.PanelWeight
import cc.trixey.mc.invero.common.Window
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Arasple
 * @since 2022/11/15 18:31
 */
abstract class BaseIOPanel(
    scale: PanelScale,
    pos: Int,
    weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    val storage = ConcurrentHashMap<Int, ItemStack>()

    fun updateStorage(window: Window) {
        val slotMap = getSlotsMap(window)

        // TODO 需要改进的逻辑
        for (key in storage.keys) {
            storage[key] = window.inventorySet[slotMap.getAbsolute(key)] ?: ItemStack(Material.AIR)
        }
    }

    operator fun set(index: Int, itemStack: ItemStack) {
        storage[index] = itemStack
    }

    operator fun get(index: Int): ItemStack? {
        return storage[index]
    }

}
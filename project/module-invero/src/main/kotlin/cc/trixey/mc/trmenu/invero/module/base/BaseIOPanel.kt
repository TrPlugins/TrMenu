package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Arasple
 * @since 2022/11/15 18:31
 */
abstract class BaseIOPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    val storage = ConcurrentHashMap<Int, ItemStack>()

    fun updateStorage(window: Window) {
        val slotMap = getSlotsMap(window)

        // TODO 需要改进的逻辑
        for (key in storage.keys) {
            storage[key] = window.inventorySet[slotMap.getActual(key)] ?: ItemStack(Material.AIR)
        }
    }

    operator fun set(index: Int, itemStack: ItemStack) {
        storage[index] = itemStack
    }

    operator fun get(index: Int): ItemStack? {
        return storage[index]
    }

}
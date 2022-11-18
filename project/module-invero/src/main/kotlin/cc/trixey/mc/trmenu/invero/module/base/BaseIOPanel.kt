package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelWeight
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
) : BasePanel(scale, pos, weight) {

    private val storage = ConcurrentHashMap<Int, ItemStack>()

    operator fun set(index: Int, itemStack: ItemStack) {
        storage[index] = itemStack
    }

    operator fun get(index: Int): ItemStack? {
        return storage[index]
    }

}
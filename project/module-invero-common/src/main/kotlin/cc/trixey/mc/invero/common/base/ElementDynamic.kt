package cc.trixey.mc.invero.common.base

import cc.trixey.mc.invero.common.Panel

/**
 * @author Arasple
 * @since 2022/11/5 14:30
 *
 * 可更改槽位的动态元素
 */
abstract class ElementDynamic(panel: Panel) : Interactable(panel) {

    internal var slots = setOf<Int>()
        set(value) {
            val diff = field - value
            field = value
            forWindows {
                val slotMap = slotMap()
                diff.forEach { inventorySet[slotMap.getAbsolute(it)] = null }
            }
            render()
        }

    fun slots(vararg slots: Int) {
        this.slots = slots.toSet()
    }

    fun slots(slots: Set<Int>) {
        this.slots = slots
    }

}
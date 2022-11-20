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
            forWindows {
                val previous = field
                (previous - value).forEach { inventorySet[slotMap().getAbsolute(it)] = null }
            }
            field = value.also {
                render()
            }
        }

    fun slots(vararg slots: Int) {
        this.slots = slots.toSet()
    }

    fun slots(slots: Set<Int>) {
        this.slots = slots
    }

}
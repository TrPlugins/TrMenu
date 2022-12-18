package cc.trixey.mc.invero.common.item

import cc.trixey.mc.invero.common.Panel

/**
 * @author Arasple
 * @since 2022/11/5 14:30
 *
 * 可更改槽位的动态元素
 */
abstract class ElementDynamic(panel: Panel) : Interactable(panel) {

    var slots = setOf<Int>()
        set(value) {
            (field - value).let { if (it.isNotEmpty()) panel.clearPanel(it) }
            field = value
            render()
        }

    fun slots(vararg slots: Int) {
        this.slots = slots.toSet()
    }

    fun slots(slots: Set<Int>) {
        this.slots = slots
    }

}
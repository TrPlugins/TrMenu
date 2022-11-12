package cc.trixey.mc.trmenu.invero.module.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Window
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @since 2022/11/5 14:30
 *
 * Dynamic element is a special kind of PanelElement
 * It does not have a specificed position, that is to say the slot of dynamic element is variable
 */
abstract class ElementDynamic(panel: Panel) : Interactable(panel) {

    internal var slots = setOf<Int>()
        set(value) {
            forWindows {
                val previous = getAbsoluteSlots()
                (previous - value).forEach { pairedInventory[it] = null }
                submit { render(this@forWindows) }
            }
            field = value
        }

    fun slots(vararg slots: Int) {
        this.slots = slots.toSet()
    }

    fun slots(slots: Set<Int>) {
        this.slots = slots
    }

    fun Window.getAbsoluteSlots(): List<Int> {
        return slots.map { slotMap().getActual(it) }.filter { it >= 0 }
    }

}
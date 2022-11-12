package cc.trixey.mc.trmenu.invero.module.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.Window

/**
 * @author Arasple
 * @since 2022/11/6 15:17
 */
abstract class ElementAbsolute(panel: Panel) : Interactable(panel) {

    private val absoltueSlots = LinkedHashMap<Int, List<Int>>()

    val slots by lazy {
        panelElements.findElementSlots(this).ifEmpty { null }
    }

    fun Window.getAbsoluteSlots(): List<Int> {
        return absoltueSlots.computeIfAbsent(type.width) {
            slots!!.map { slotMap().getActual(it) }.filter { it >= 0 }
        }
    }

}
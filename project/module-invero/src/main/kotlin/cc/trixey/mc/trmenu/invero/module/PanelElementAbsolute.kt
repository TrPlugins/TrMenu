package cc.trixey.mc.trmenu.invero.module

/**
 * @author Arasple
 * @since 2022/11/6 15:17
 */
abstract class PanelElementAbsolute(override val parentPanel: Panel) : PanelElement {

    private val actualSlots = LinkedHashMap<Int, List<Int>>()

    override val relativeSlotsInParentPanel by lazy {
        parentPanelElements.findElement(this).ifEmpty { null }
    }

    fun Window.actualSlots(): List<Int> {
        return actualSlots.computeIfAbsent(type.width) {
            relativeSlotsInParentPanel!!.map { slotMap().getActual(it) }.filter { it >= 0 }
        }
    }

}
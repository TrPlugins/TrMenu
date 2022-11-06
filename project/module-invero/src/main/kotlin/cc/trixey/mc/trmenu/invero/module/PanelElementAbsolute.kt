package cc.trixey.mc.trmenu.invero.module

/**
 * @author Arasple
 * @since 2022/11/6 15:17
 */
abstract class PanelElementAbsolute(override val panel: Panel) : PanelElement {

    private val actualSlots = LinkedHashMap<Int, List<Int>>()

    override val relativeSlots by lazy {
        panelElements.entries.filter { it.value == this }.map { it.key }.let {
            it.ifEmpty { null }
        }
    }

    fun Window.actualSlots(): List<Int> {
        return actualSlots.computeIfAbsent(type.width) {
            relativeSlots!!.map { slotMap().getActual(it) }.filter { it >= 0 }
        }
    }

}
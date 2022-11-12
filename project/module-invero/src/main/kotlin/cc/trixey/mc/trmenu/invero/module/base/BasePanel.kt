package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelElement
import cc.trixey.mc.trmenu.invero.module.PanelElementDynamic
import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/6 16:47
 */
abstract class BasePanel(scale: Pair<Int, Int>, pos: Int, weight: PanelWeight) : PanelInstance(scale, pos, weight) {

    private val elementsMap = MappedElements()

    override val slotsOccupied: Set<Int>
        get() = elementsMap.slotsOccupied


    override val slotsUnoccupied: List<Int>
        get() = slots - slotsOccupied

    fun setElement(relativeSlot: Int, element: PanelElement) {
        elementsMap[relativeSlot] = element
    }

    fun getElement(relativeSlot: Int): PanelElement? {
        return elementsMap[relativeSlot]
    }

    fun addElementDynamic(element: PanelElementDynamic) {
        elementsMap += element
    }

    fun removeElementDynamic(element: PanelElementDynamic) {
        elementsMap -= element
    }

    fun getElements(): MappedElements {
        return elementsMap
    }

    override fun render(window: Window) {
        elementsMap.forEach { _, panelElement ->
            panelElement.render(window)
        }
    }

}
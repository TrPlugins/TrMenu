package cc.trixey.mc.invero.impl.container.element

import cc.trixey.mc.invero.item.ElemapCompetent
import cc.trixey.mc.invero.Element
import cc.trixey.mc.invero.impl.container.panel.PagedStandardPanel

/**
 * @author Arasple
 * @since 2022/11/23 12:59
 */
class ElemapPaged(val index: Int, override val panel: PagedStandardPanel) : ElemapCompetent(panel) {

    fun Element.fillup() {
        set((panel as PagedStandardPanel).getUnoccupiedSlots(index))
    }

}
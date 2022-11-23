package cc.trixey.mc.invero.element

import cc.trixey.mc.invero.common.ElemapCompetent
import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.panel.PagedStandardPanel

/**
 * @author Arasple
 * @since 2022/11/23 12:59
 */
class ElemapPaged(val index: Int, override val panel: PagedStandardPanel) : ElemapCompetent(panel) {

    fun Element.fillup() {
        set((panel as PagedStandardPanel).getUnoccupiedSlots(index))
    }

}
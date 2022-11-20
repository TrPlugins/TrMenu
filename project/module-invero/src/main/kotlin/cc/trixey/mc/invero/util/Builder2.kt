package cc.trixey.mc.invero.util

import cc.trixey.mc.invero.common.MappedElements
import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.Window
import cc.trixey.mc.invero.common.base.ElementAbsolute
import cc.trixey.mc.invero.common.base.PanelInstance
import cc.trixey.mc.invero.panel.PagedNetesedPanel
import cc.trixey.mc.invero.panel.PagedStandardPanel

/**
 * @author Arasple
 * @since 2022/11/20 16:54
 */
fun PagedStandardPanel.page(function: MappedElements.(Int) -> Unit) {
    return MappedElements().let { function(it, addPage(it)) }
}

fun PagedNetesedPanel.page(panel: PanelInstance) {
    addPage(panel)
}

fun PanelInstance.paged(parent: PagedNetesedPanel) {
    parent.page(this)
}

fun ElementAbsolute.setDefault(panel: PagedStandardPanel, vararg slots: Int) {
    panel.getStaticElements().apply { set(*slots) }
}

fun Window.addPanel(vararg panel: Panel) {
    panel.forEach { panels += it }
}
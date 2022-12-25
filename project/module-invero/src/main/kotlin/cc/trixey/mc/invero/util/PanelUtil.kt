package cc.trixey.mc.invero.util

import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.PanelContainer

/**
 * @author Arasple
 * @since 2022/11/22 22:53
 */
fun PanelContainer.findPanel(slot: Int): Panel? {
    return panels.sortedByDescending { it.weight }.find { it.scale.getCache()!!.containsValue(slot) }
}

fun PanelContainer.findPanel(slots: Collection<Int>) = findPanel(slots.first())
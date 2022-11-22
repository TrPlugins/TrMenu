package cc.trixey.mc.invero.common.util

import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.Parentable
import cc.trixey.mc.invero.common.base.BaseWindow
import cc.trixey.mc.invero.common.universal.PanelGroup

/**
 * @author Arasple
 * @since 2022/11/22 22:53
 */
fun PanelGroup.findPanel(slot: Int, parent: Parentable? = this): Panel? {
    return panels.sortedByDescending { it.weight }.firstOrNull {
        it.getSlotsMap(parent).absoluteSlots.contains(slot)
    }
}

fun BaseWindow.findPanel(slot: Int, parent: Parentable? = this): Panel? {
    return panels.sortedByDescending { it.weight }.firstOrNull {
        it.getSlotsMap(parent).absoluteSlots.contains(slot)
    }
}

fun BaseWindow.findPanel(slots: Collection<Int>, parent: Parentable? = this): Panel? {
    return panels.sortedByDescending { it.weight }.firstOrNull {
        it.getSlotsMap(parent).absoluteSlots.any { slot -> slot in slots }
    }
}
package cc.trixey.mc.invero.common.util

import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.Window
import cc.trixey.mc.invero.common.base.BaseWindow
import cc.trixey.mc.invero.common.universal.PanelGroup

/**
 * @author Arasple
 * @since 2022/11/22 22:53
 */
fun PanelGroup.findPanel(slot: Int, window: Window): Panel? {
    return panels.sortedByDescending { it.weight }.firstOrNull {
        it.getSlotsMap(window).absoluteSlots.contains(slot)
    }
}

fun BaseWindow.findPanel(slot: Int): Panel? {
    return panels.sortedByDescending { it.weight }.firstOrNull {
        it.getSlotsMap(this).absoluteSlots.contains(slot)
    }
}

fun BaseWindow.findPanel(slots: Collection<Int>): Panel? {
    return panels.sortedByDescending { it.weight }.firstOrNull {
        it.getSlotsMap(this).absoluteSlots.any { slot -> slot in slots }
    }
}
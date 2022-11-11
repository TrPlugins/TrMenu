package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.module.base.BasePagedPanel
import cc.trixey.mc.trmenu.invero.module.base.BasePanel
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/1 22:00
 */
interface PanelElement {

    /**
     * The panel to which this element belongs to
     */
    val parentPanel: Panel

    val parentPanelElements: MappedElements
        get() {
            return if (parentPanel is BasePanel) {
                (parentPanel as BasePanel).getElements()
            } else {
                (parentPanel as BasePagedPanel).getPage()
            }
        }

    val appliedWindows: LinkedList<Window>
        get() {
            return parentPanel.windows
        }

    val relativeSlotsInParentPanel: Set<Int>?

    fun Window.slotMap() = parentPanel.getSlotsMap(this)

    fun forWindows(function: Window.() -> Unit) = parentPanel.forWindows(function)

    fun render(window: Window)

}
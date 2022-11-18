package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.RollingDirection
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute

/**
 * @author Arasple
 * @since 2022/11/17 11:19
 */
abstract class BaseScrollPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight,
    val direction: RollingDirection = RollingDirection(vertical = true)
) : PanelInstance(scale, pos, weight) {

    /*
    vertical scrolling:
    Fixed width, unlimited height (lines), score [lines]

    horizontal scrolling:
    Fixed height(lines), unlimited width, scroll [rows]
     */

    val colums = mutableListOf<Array<ElementAbsolute>>()

}

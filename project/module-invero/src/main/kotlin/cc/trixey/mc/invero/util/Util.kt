package cc.trixey.mc.invero.util

import cc.trixey.mc.invero.common.PanelScale

/**
 * @author Arasple
 * @since 2022/11/20 16:54
 */
fun Pair<Int, Int>.toScale(): PanelScale {
    return PanelScale(this)
}
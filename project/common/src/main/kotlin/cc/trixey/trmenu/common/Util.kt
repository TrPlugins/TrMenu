package cc.trixey.trmenu.common

import cc.trixey.trmenu.common.animation.Cycle
import cc.trixey.trmenu.common.animation.CycleArray
import cc.trixey.trmenu.common.animation.CycleMode
import cc.trixey.trmenu.common.animation.CycleSingle

/**
 * TrMenu
 * cc.trixey.trmenu.common.Util
 *
 * @author Arasple
 * @since 2023/1/13 12:24
 */

fun Array<String>.createCyclable(loopMode: CycleMode): Cycle<String> {
    return if (size == 1) CycleSingle(first())
    else CycleArray(this, loopMode)
}
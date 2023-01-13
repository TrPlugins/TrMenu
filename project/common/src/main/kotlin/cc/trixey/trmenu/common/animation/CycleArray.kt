package cc.trixey.trmenu.common.animation

/**
 * TrMenu
 * cc.trixey.trmenu.common.animation.CycleString
 *
 * @author Arasple
 * @since 2023/1/13 12:26
 */
class CycleArray(private val value: Array<String>, private val mode: CycleMode = CycleMode.LOOP) : Cycle<String> {

    var index: Int = 0

    val maxIndex = value.indices.last

    var reversing: Boolean = false

    init {
        if (maxIndex == 0) error("Use CycleSingle instead of CycleArray for singled element")
    }

    override fun get(): String {
        return value[index]
    }

    override fun getMode(): CycleMode {
        return mode
    }

    override fun cycle() {
        if (mode.isRandom) {
            index = value.indices.random()
        } else if (mode.isLoop) {
            if (index == maxIndex) index = 0
            else index++
        } else if (mode.isReversable) {
            if (reversing) {
                index--
                if (index == 0) reversing = false
            } else {
                index++
                if (index == maxIndex) reversing = true
            }
        }
    }

}
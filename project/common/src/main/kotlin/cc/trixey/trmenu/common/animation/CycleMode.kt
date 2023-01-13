package cc.trixey.trmenu.common.animation

/**
 * TrMenu
 * cc.trixey.trmenu.common.animation.CycleMode
 *
 * @author Arasple
 * @since 2023/1/13 12:29
 */
@JvmInline
value class CycleMode(val mode: Int = 0) {

    val isLoop: Boolean
        get() = mode == 0

    val isReversable: Boolean
        get() = mode < 0

    val isRandom: Boolean
        get() = mode > 0

    companion object {

        val LOOP = CycleMode(0)

        val REVERSABLE = CycleMode(-1)

        val RANDOM = CycleMode(1)

    }

}
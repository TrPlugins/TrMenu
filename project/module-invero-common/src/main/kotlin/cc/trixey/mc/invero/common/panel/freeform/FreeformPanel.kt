package cc.trixey.mc.invero.common.panel.freeform

/**
 * @author Arasple
 * @since 2022/11/24 15:28
 */
interface FreeformPanel {

    fun reset()

    fun shift(x: Int = 0, y: Int = 0)

    fun left() = shift(-1)

    fun right() = shift(1)

    fun up() = shift(y = -1)

    fun down() = shift(y = 1)

    fun upLeft() = shift(-1, -1)

    fun upRight() = shift(1, -1)

    fun downLeft() = shift(-1, 1)

    fun downRight() = shift(1, 1)

}
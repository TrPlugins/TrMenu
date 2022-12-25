package cc.trixey.mc.invero

/**
 * @author Arasple
 * @since 2022/11/1 22:00
 */
interface Element {

    /**
     * 元素所属 Panel
     */
    val panel: Panel

    fun forWindows(function: Window.() -> Unit) = panel.forWindows(function)

    fun render()

}
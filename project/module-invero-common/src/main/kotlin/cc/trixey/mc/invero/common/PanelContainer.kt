package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/11/23 12:24
 */
interface PanelContainer {

    fun getPanels(): List<Panel>

    fun addPanel(panel: Panel): Boolean

    fun removePanel(panel: Panel): Boolean

}
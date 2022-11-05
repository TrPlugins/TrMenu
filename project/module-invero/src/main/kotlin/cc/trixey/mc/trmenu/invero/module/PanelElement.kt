package cc.trixey.mc.trmenu.invero.module

import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/1 22:00
 */
interface PanelElement {

    /**
     * The panel to which this element belongs to
     */
    val panel: Panel

    fun renderItem(): ItemStack?

}
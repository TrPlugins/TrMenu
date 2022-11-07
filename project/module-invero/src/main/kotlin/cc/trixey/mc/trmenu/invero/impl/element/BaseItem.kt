package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.PanelElementAbsolute
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem

/**
 * @author Arasple
 * @since 2022/11/4 18:37
 */
open class BaseItem(private var itemStack: ItemStack?, panel: Panel) : PanelElementAbsolute(panel) {

    private var handlerClick: (e: InventoryClickEvent) -> Unit = {}

    fun setItem(material: Material, itemBuilder: ItemBuilder.() -> Unit = {}) {
        this.itemStack = buildItem(material, itemBuilder)
    }

    fun modify(itemBuilder: ItemBuilder.() -> Unit) {
        itemStack?.let { buildItem(itemStack!!, itemBuilder) }
    }

    fun onClick(pass: InventoryClickEvent.() -> Unit) {
        handlerClick = pass
    }

    override fun handleClick(e: InventoryClickEvent) {
        handlerClick(e)
    }

    override fun render(window: Window) {
        window.actualSlots().forEach {
            window.pairedInventory[it] = itemStack
        }
    }

}
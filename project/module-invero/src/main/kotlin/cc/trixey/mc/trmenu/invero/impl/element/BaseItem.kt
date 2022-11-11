package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Clickable
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
open class BaseItem(private var itemStack: ItemStack?, panel: Panel) : PanelElementAbsolute(panel), Clickable {

    override var handler: (e: InventoryClickEvent) -> Unit = {}

    fun setItem(material: Material, builder: ItemBuilder.() -> Unit = {}) {
        itemStack = buildItem(material, builder)
    }

    fun setItem(value: ItemStack?) {
        itemStack = value
    }

    fun modify(builder: ItemBuilder.() -> Unit) {
        itemStack?.let { buildItem(itemStack!!, builder) }
    }

    override fun render(window: Window) {
        window.actualSlots().forEach {
            window.pairedInventory[it] = itemStack
        }
    }

}
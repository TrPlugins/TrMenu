package cc.trixey.mc.trmenu.invero.impl.element

import cc.trixey.mc.trmenu.invero.module.Clickable
import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.PanelElementAbsolute
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import taboolib.platform.util.modifyLore
import taboolib.platform.util.modifyMeta
import java.util.function.Supplier

/**
 * @author Arasple
 * @since 2022/11/4 18:37
 */
open class BasicItem(private var value: ItemStack?, panel: Panel) : PanelElementAbsolute(panel), Clickable {

    override var handler: (e: InventoryClickEvent) -> Unit = {}

    fun setItemAsync(supplier: Supplier<ItemStack?>) {
        submitAsync {
            setItem(supplier.get())
        }
    }

    fun setItem(material: Material, builder: ItemBuilder.() -> Unit = {}) {
        this.value = buildItem(material, builder)
    }

    fun setItem(value: ItemStack?) {
        this.value = value
    }

    fun modify(func: ItemBuilder.() -> Unit) {
        this.value?.let { buildItem(value!!, func) }
    }

    fun <T : ItemMeta> modifyMeta(func: T.() -> Unit) = value?.modifyMeta(func)

    fun modifyLore(func: MutableList<String>.() -> Unit) = value?.modifyLore(func)

    override fun render(window: Window) {
        window.actualSlots().forEach {
            window.pairedInventory[it] = value
        }
    }

}
package cc.trixey.mc.invero.impl.container.element

import cc.trixey.mc.invero.ItemProvider
import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.item.ElementDynamic
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/12 19:52
 */
class BasicDynamicItem(private var itemStack: ItemStack?, panel: Panel) : ElementDynamic(panel), ItemProvider {

    override fun render() {
        forWindows { panel.renderElement(this@forWindows, this@BasicDynamicItem) }
    }

    override fun get(): ItemStack? {
        return itemStack
    }

    override fun setItem(itemStack: ItemStack?) {
        this.itemStack = itemStack
    }

}
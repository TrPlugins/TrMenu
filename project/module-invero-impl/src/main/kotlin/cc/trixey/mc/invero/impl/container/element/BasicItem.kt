package cc.trixey.mc.invero.impl.container.element

import cc.trixey.mc.invero.ItemProvider
import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.item.ElementAbsolute
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/11/4 18:37
 */
class BasicItem(private var itemStack: ItemStack?, panel: Panel) : ElementAbsolute(panel), ItemProvider {

    override fun render() {
        forWindows { panel.renderElement(this@forWindows, this@BasicItem) }
    }

    override fun get(): ItemStack? {
        return itemStack
    }

    override fun setItem(itemStack: ItemStack?) {
        this.itemStack = itemStack
    }

}
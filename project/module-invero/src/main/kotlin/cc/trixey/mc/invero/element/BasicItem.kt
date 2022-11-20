package cc.trixey.mc.invero.element

import cc.trixey.mc.invero.common.ItemProvider
import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.base.ElementAbsolute
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
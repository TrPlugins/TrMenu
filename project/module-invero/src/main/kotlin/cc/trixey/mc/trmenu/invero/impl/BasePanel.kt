package cc.trixey.mc.trmenu.invero.impl

import cc.trixey.mc.trmenu.invero.module.Panel
import cc.trixey.mc.trmenu.invero.module.PanelElement
import cc.trixey.mc.trmenu.invero.module.PanelWeight
import cc.trixey.mc.trmenu.invero.module.Window
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/11/1 21:33
 */
class BasePanel(
    override val window: Window,
    override var weight: PanelWeight = PanelWeight.NORMAL,
    override val elements: LinkedList<PanelElement> = LinkedList()
) : Panel {

    /**
     * Claimed Slots
     */
    val claimed: CopyOnWriteArrayList<Int> = CopyOnWriteArrayList()


    // TODO IMPROVABLE PROTOTYPE
    fun claim(slot: Int) {
        claimed.add(slot)
    }

    // TODO IMPROVABLE PROTOTYPE
    fun item(itemStack: ItemStack, distribute: Int) {
        elements.add(BaseItem(itemStack, distribute))
    }

    // TODO IMPROVABLE PROTOTYPE
    override fun render() {
        elements.forEach {
            when (it) {
                is BaseItem -> {
                    window.pairedInventory.container.setItem(it.distribute, it.itemStack)
                }
            }
        }
    }

    override fun getChildren() = null

}
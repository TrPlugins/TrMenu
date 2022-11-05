package cc.trixey.mc.trmenu.invero.impl.item

import cc.trixey.mc.trmenu.invero.module.PanelElement
import org.bukkit.inventory.ItemStack
import java.util.function.Supplier

/**
 * @author Arasple
 * @since 2022/11/5 17:58
 */
interface ItemProvier : Supplier<ItemStack>, PanelElement {

    override fun renderItem() = get()

}
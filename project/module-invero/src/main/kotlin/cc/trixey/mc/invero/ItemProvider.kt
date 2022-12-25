package cc.trixey.mc.invero

import org.bukkit.Material
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
 * @since 2022/11/12 19:56
 */
interface ItemProvider : Supplier<ItemStack?> {

    fun setItem(itemStack: ItemStack?)

    fun setItem(material: Material, builder: ItemBuilder.() -> Unit = {}) {
        setItem(buildItem(material, builder))
    }

    fun setItemAsync(supplier: Supplier<ItemStack?>) {
        submitAsync { setItem(supplier.get()) }
    }

    fun modify(builder: ItemBuilder.() -> Unit) {
        get()?.let { setItem(buildItem(it, builder)) }
    }

    fun <T : ItemMeta> modifyMeta(func: T.() -> Unit) {
        get()?.modifyMeta(func)
    }

    fun modifyLore(func: MutableList<String>.() -> Unit) {
        get()?.modifyLore(func)
    }

}
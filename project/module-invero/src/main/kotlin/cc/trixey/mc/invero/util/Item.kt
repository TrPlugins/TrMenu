package cc.trixey.mc.invero.util

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.modifyMeta


/**
 * @author Arasple
 * @since 2022/11/20 16:32
 */
private val NAMESPACED_KEY = NamespacedKey(BukkitPlugin.getInstance(), "invero_slot")

fun ItemStack?.distinguishMark(slot: Int): ItemStack? {
    return this?.modifyMeta<ItemMeta> {
        persistentDataContainer.set(NAMESPACED_KEY, PersistentDataType.BYTE, slot.toByte())
    }
}
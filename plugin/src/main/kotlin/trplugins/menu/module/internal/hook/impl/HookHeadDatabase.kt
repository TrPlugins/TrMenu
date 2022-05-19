package trplugins.menu.module.internal.hook.impl

import trplugins.menu.module.internal.hook.HookAbstract
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem

/**
 * @author Arasple
 * @date 2021/1/26 22:04
 */
class HookHeadDatabase : HookAbstract() {

    private val headDatabaseAPI: HeadDatabaseAPI? = if (isHooked) HeadDatabaseAPI() else null
        get() {
            if (field == null) reportAbuse()
            return field
        }

    private val empty = buildItem(XMaterial.PLAYER_HEAD) { name = "UNHOOKED_${super.name.uppercase()}" }

    fun getHead(id: String): ItemStack {
        return headDatabaseAPI?.getItemHead(id) ?: empty
    }

    fun getRandomHead(): ItemStack {
        return headDatabaseAPI?.randomHead ?: empty
    }

    fun getId(itemStack: ItemStack): String? {
        return headDatabaseAPI?.getItemID(itemStack)
    }

}
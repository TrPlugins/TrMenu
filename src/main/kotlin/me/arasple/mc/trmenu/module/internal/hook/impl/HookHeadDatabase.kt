package me.arasple.mc.trmenu.module.internal.hook.impl

import io.izzel.taboolib.internal.xseries.XMaterial
import io.izzel.taboolib.util.item.ItemBuilder
import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.inventory.ItemStack

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

    private val empty = ItemBuilder(XMaterial.PLAYER_HEAD).name("UNHOOKED_${name.toUpperCase()}").build()

    fun getHead(id: String): ItemStack {
        return headDatabaseAPI?.getItemHead(id) ?: empty
    }

    fun getRandomHead(): ItemStack {
        return headDatabaseAPI?.randomHead ?: empty
    }

    fun getId(itemStack: ItemStack): String {
        return headDatabaseAPI?.getItemID(itemStack) ?: "UNHOOKED"
    }

}
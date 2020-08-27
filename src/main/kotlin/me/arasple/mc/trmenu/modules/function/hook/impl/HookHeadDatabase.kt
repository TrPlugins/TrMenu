package me.arasple.mc.trmenu.modules.function.hook.impl

import io.izzel.taboolib.util.lite.Materials
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/8/27 19:59
 */
class HookHeadDatabase : HookInstance() {

    private lateinit var api: HeadDatabaseAPI
    private val empty = Materials.PLAYER_HEAD.parseItem()!!

    override fun getDepend(): String {
        return "HeadDatabase"
    }

    override fun initialization() {
        api = HeadDatabaseAPI()
    }

    fun getHead(id: String): ItemStack {
        return if (id.equals("random", true)) {
            getRandomHead()
        } else {
            api.getItemHead(id) ?: empty
        }
    }

    fun getRandomHead(): ItemStack {
        return api.randomHead
    }

    fun getId(itemStack: ItemStack): String {
        return api.getItemID(itemStack)
    }

}
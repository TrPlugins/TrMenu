package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.Materials
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/2 13:32
 */
object HookHeadDatabase {

    private const val PLUGIN_NAME = "HeadDatabase"
    private val EMPTY_ITEM = Materials.AIR.parseItem()!!
    private var HEAD_DATABASE_API: HeadDatabaseAPI? = null
    var IS_HOOKED = false

    fun init() {
        IS_HOOKED = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)?.isEnabled ?: false
        if (IS_HOOKED) {
            HEAD_DATABASE_API = HeadDatabaseAPI()
            TLocale.sendToConsole("HOOKED", PLUGIN_NAME)
        }
    }

    fun getHead(id: String): ItemStack = if (IS_HOOKED) {
        if (id.equals("random", true)) getRandomHead() else HEAD_DATABASE_API?.getItemHead(id) ?: EMPTY_ITEM
    } else {
        TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        EMPTY_ITEM
    }

    fun getRandomHead(): ItemStack = if (IS_HOOKED) {
        HEAD_DATABASE_API?.randomHead ?: EMPTY_ITEM
    } else {
        TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        EMPTY_ITEM
    }


}
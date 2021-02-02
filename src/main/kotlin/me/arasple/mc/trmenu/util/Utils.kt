package me.arasple.mc.trmenu.util

import com.google.gson.JsonParser
import io.izzel.taboolib.util.item.Items
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/24 17:39
 */
object Utils {

    fun isJson(json: String): Boolean {
        return try {
            JsonParser().parse(json)
            true
        } catch (e: Throwable) {
            false
        }
    }

    fun asJsonItem(json: String): ItemStack? {
        return try {
            Items.fromJson(json)
        } catch (e: Throwable) {
            null
        }
    }

    fun containsPlaceholder(string: String): Boolean {
        return Regexs.PLACEHOLDER_API.find(string) != null
    }

}
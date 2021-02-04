package me.arasple.mc.trmenu.util.bukkit

import com.google.gson.JsonParser
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.TrMenu
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/2/4 9:56
 */
object ItemHelper {

    fun defColorize(string: String, isLore: Boolean = false): String {
        return if (string.isNotBlank() && !string.startsWith(ChatColor.COLOR_CHAR) && !string.startsWith('&')) {
            val path = "Menu.Icon.Item.Default-Color-${if (isLore) "Lore" else "Name"}"
            val defColor = TrMenu.SETTINGS.getStringColored(path, "&7")
            defColor + string
        } else string
    }

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

}
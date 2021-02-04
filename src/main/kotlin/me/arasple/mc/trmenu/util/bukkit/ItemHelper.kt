package me.arasple.mc.trmenu.util.bukkit

import com.google.gson.JsonParser
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.TrMenu
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import kotlin.math.min

/**
 * @author Arasple
 * @date 2021/2/4 9:56
 */
object ItemHelper {

    fun serializeColor(color: String): Color {
        val rgb = color.split(",").toTypedArray()
        if (rgb.size == 3) {
            val r = min(rgb[0].toIntOrNull() ?: 0, 255)
            val g = min(rgb[1].toIntOrNull() ?: 0, 255)
            val b = min(rgb[2].toIntOrNull() ?: 0, 255)
            return Color.fromRGB(r, g, b)
        }
        return Color.BLACK
    }

    fun deserializeColor(color: Color): String {
        return "${color.red},${color.green},${color.blue}"
    }

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
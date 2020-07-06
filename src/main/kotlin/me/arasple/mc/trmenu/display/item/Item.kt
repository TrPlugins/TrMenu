package me.arasple.mc.trmenu.display.item

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.ChatColor

/**
 * @author Arasple
 * @date 2020/7/6 17:07
 */
object Item {

    fun colorizeLore(string: String) = colorize(string, true)

    fun colorizeName(string: String) = colorize(string, false)

    private fun colorize(string: String, isLore: Boolean) =
        if (!string.startsWith(ChatColor.COLOR_CHAR) && !string.startsWith('&'))
            TrMenu.SETTINGS.getStringColored("Default-Color-${if (isLore) "Lore" else "Name"}") + string
        else string

}
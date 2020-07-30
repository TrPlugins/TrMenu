package me.arasple.mc.trmenu.display.item

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import io.izzel.taboolib.util.item.ItemBuilder
import me.arasple.mc.trmenu.TrMenu
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple,Sky
 * @date 2020/7/6 17:07
 */
object Item {

    fun colorizeLore(string: String) = colorize(string, true)

    fun colorizeName(string: String) = colorize(string, false)

    fun fromJson(item: String): ItemStack {
        val json = JsonParser().parse(TLocale.Translate.setColored(item))
        if (json is JsonObject) {
            val itemBuilder = ItemBuilder(Material.STONE)
            json["type"]?.also { itemBuilder.material(Material.valueOf(it.asString)) }
            json["data"]?.also { itemBuilder.damage(it.asInt) }
            json["amount"]?.also { itemBuilder.amount(it.asInt) }
            val itemBuild = itemBuilder.build()
            val meta = json["meta"]
            return if (meta != null) {
                NMS.handle().saveNBT(itemBuild, NBTCompound.fromJson(meta.toString()))
            } else itemBuild
        }
        return ItemStack(Material.BARRIER)
    }

    private fun colorize(string: String, isLore: Boolean) =
        if (string.isNotBlank() && !string.startsWith(ChatColor.COLOR_CHAR) && !string.startsWith('&'))
            TrMenu.SETTINGS.getStringColored("Item.Default-Color-${if (isLore) "Lore" else "Name"}", "&7") + string
        else string

}
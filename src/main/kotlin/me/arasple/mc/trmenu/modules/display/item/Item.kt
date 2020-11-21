package me.arasple.mc.trmenu.modules.display.item

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Materials
import me.arasple.mc.trmenu.TrMenu
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple,Sky
 * @date 2020/7/6 17:07
 */
object Item {

    val EMPTY_ITEM = ItemStack(Material.AIR)

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
        return EMPTY_ITEM
    }

    fun fromMaterials(name: String) =
            Materials.values().firstOrNull {
                it.name == name
            } ?: Materials.values().firstOrNull { it.legacy.any { legacy -> legacy == name } } ?: Materials.values().maxByOrNull {
                Strings.similarDegree(name, it.name)
            }

    private fun colorize(string: String, isLore: Boolean) =
            if (string.isNotBlank() && !string.startsWith(ChatColor.COLOR_CHAR) && !string.startsWith('&'))
                TrMenu.SETTINGS.getStringColored("Menu.Item.Default-Color-${if (isLore) "Lore" else "Name"}", "&7") + string
            else string

}
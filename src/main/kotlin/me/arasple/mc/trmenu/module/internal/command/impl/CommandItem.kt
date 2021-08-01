package me.arasple.mc.trmenu.module.internal.command.impl

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.arasple.mc.trmenu.module.internal.item.ItemRepository
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import me.arasple.mc.trmenu.util.net.Paster
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.subCommand
import taboolib.library.xseries.XSound
import taboolib.module.nms.getItemTag
import taboolib.platform.util.sendLang
import taboolib.type.BukkitEquipment

/**
 * @author Arasple
 * @date 2021/1/31 10:41
 */
object CommandItem {

    internal fun toJson(player: Player, item: ItemStack) {
        val json = JsonObject()
        json.addProperty("type", item.type.name)
        json.addProperty("data", item.data!!.data)
        json.addProperty("amount", item.amount)
        json.add("meta", JsonParser().parse(item.getItemTag().toJson()))
        val stringJson = json.toString()
        if (stringJson.length < 200) player.sendLang("Command-Item-To-Json", stringJson)
        else Paster.paste(player, stringJson, "json")
    }

    internal fun fromJson(player: Player, json: String) {
        ItemHelper.fromJson(json)?.let {
            player.inventory.addItem(it).values.forEach { e -> player.world.dropItem(player.location, e) }
        }
    }

}
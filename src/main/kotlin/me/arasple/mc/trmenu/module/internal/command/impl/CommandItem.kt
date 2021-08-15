package me.arasple.mc.trmenu.module.internal.command.impl

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.arasple.mc.trmenu.module.internal.command.CommandExpresser
import me.arasple.mc.trmenu.module.internal.item.ItemRepository
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import me.arasple.mc.trmenu.util.net.Paster
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XSound
import taboolib.module.nms.getItemTag
import taboolib.platform.util.sendLang
import taboolib.type.BukkitEquipment

/**
 * @author Arasple
 * @date 2021/1/31 10:41
 */
object CommandItem : CommandExpresser {

    // toJson -NoValueNeeded-
    // fromJson [value]
    // save [id]
    // get [id]
    // del [id]

    // menu item [Method] <Value>
    override val command = subCommand {
        // Method
        dynamic {
            suggestion<CommandSender> { _, _ ->
                listOf("toJson", "fromJson", "save", "get", "del")
            }
            // Value
            dynamic {
                execute<CommandSender> { player, context, argument ->
                    XSound.ITEM_BOTTLE_FILL.play(player as Player, 1f, 0f)

                    val item = BukkitEquipment.getItems(player)[BukkitEquipment.HAND]

                    if (context.argument(-1).equals("toJson", true)) {
                        item ?: kotlin.run {
                            player.sendLang("Command-Item-No-Item")
                            return@execute
                        }
                        toJson(player, item)
                    } else {
                        when (context.argument(-1)?.lowercase()) {
                            "fromjson" -> fromJson(player, argument)
                            "get" -> ItemRepository.getItem(argument)?.let {
                                player.inventory.addItem(it).values.forEach { e ->
                                    player.world.dropItem(
                                        player.location,
                                        e
                                    )
                                }
                            }
                            "save" -> item?.let {
                                ItemRepository.getItemStacks()[argument] = item
                                player.sendLang("Command-Item-Saved", argument)
                            }
                            "delete" -> ItemRepository.removeItem(argument)?.let {
                                player.sendLang("Command-Item-Deleted", argument)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun toJson(player: Player, item: ItemStack) {
        val json = JsonObject()
        json.addProperty("type", item.type.name)
        json.addProperty("data", item.data!!.data)
        json.addProperty("amount", item.amount)
        json.add("meta", JsonParser().parse(item.getItemTag().toJson()))
        val stringJson = json.toString()
        if (stringJson.length < 200) player.sendLang("Command-Item-To-Json", stringJson)
        else Paster.paste(player, stringJson, "json")
    }

    private fun fromJson(player: Player, json: String) {
        ItemHelper.fromJson(json)?.let {
            player.inventory.addItem(it).values.forEach { e -> player.world.dropItem(player.location, e) }
        }
    }

}
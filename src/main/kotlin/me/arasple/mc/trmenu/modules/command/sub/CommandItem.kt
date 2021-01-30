package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.Version
import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import me.arasple.mc.trmenu.util.Paster
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.modules.display.item.Item
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


/**
 * @author Arasple
 * @date 2020/7/22 11:43
 */
class CommandItem : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Type", true) {
            listOf("toJson", "fromJson")
        }
    )

    override fun getType(): CommandType = CommandType.PLAYER

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = sender as Player
        when (args[0].toLowerCase()) {
            "tojson" -> toJson(player)
            "fromjson" -> fromJson(player, args.joinToString(""))
        }
    }

    private fun toJson(player: Player) {
        @Suppress("DEPRECATION")
        val item = if (Version.isAfter(Version.v1_9)) player.inventory.itemInMainHand else player.inventory.itemInHand
        if (Items.isNull(item)) {
            player.sendLocale("COMMANDS.ITEM.TO-JSON.NO-ITEM")
            return
        }

        val json = Items.toJson(item)
        if (json.length < 200) {
            player.sendLocale("COMMANDS.ITEM.TO-JSON.CONVERTED", json)
        } else {
            player.sendLocale("HASTEBIN.PROCESSING")
            Tasks.task(true) {
                val url = Paster.paste(json)
                player.sendLocale(if (url != null) "HASTEBIN.SUCCESS" else "HASTEBIN.FAILED", url ?: "")
            }
        }
    }

    private fun fromJson(player: Player, json: String) {
        CronusUtils.addItem(player, Item.fromJson(json))
    }

}
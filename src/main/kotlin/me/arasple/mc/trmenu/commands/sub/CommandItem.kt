package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Hastebin
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.display.item.Item
import me.arasple.mc.trmenu.utils.Tasks
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
            "fromJson" -> fromJson(player, args.joinToString(""))
        }
    }

    private fun toJson(player: Player) {
        val item = player.inventory.itemInMainHand
        if (Items.isNull(item)) {
            TLocale.sendTo(player, "COMMANDS.ITEM.TO-JSON.NO-ITEM")
            return
        }

        val json = Items.toJson(item)
        if (json.length < 200) {
            TLocale.sendTo(player, "COMMANDS.ITEM.TO-JSON.CONVERTED", json)
        } else {
            TLocale.sendTo(player, "HASTEBIN.PROCESSING")
            Tasks.task(true) {
                val url = Hastebin.paste(json).url
                TLocale.sendTo(player, if (url != null) "HASTEBIN.SUCCESS" else "HASTEBIN.FAILED", url)
            }
        }
    }

    private fun fromJson(player: Player, json: String) {
        CronusUtils.addItem(player, Item.fromJson(json))
    }

}
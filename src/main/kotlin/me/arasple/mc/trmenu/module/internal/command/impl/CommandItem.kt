package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.item.Equipments
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.module.internal.item.ItemRepository
import me.arasple.mc.trmenu.util.net.Paster
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/31 10:41
 */
class CommandItem : BaseSubCommand() {

    override fun getArguments(): Array<Argument> {
        return arrayOf(
            Argument("Method") {
                listOf("toJson", "fromJson", "save", "get", "delete")
            },
            // key
            Argument("Value", false)
        )
    }

    // toJson -NoValueNeeded-
    // fromJson [value]
    // save [id]
    // get [id]
    // del [id]

    override fun onCommand(player: CommandSender, command: Command, label: String, args: Array<String>) {
        Sounds.ITEM_BOTTLE_FILL.play(player as Player, 1f, 0f)

        val value = args.getOrNull(1)
        val item = Equipments.getItems(player)[Equipments.HAND]

        if (args[0].equals("toJson", true)) {
            item ?: kotlin.run {
                TLocale.sendTo(player, "Command.Item.No-Item")
                return
            }
            toJson(player, item)
        } else if (value != null) {
            when (args[0].toLowerCase()) {
                "fromjson" -> fromJson(player, value)
                "get" -> ItemRepository.getItem(value)?.let { CronusUtils.addItem(player, it) }
                "save" -> item?.let {
                    ItemRepository.getItemStacks()[value] = item
                    TLocale.sendTo(player, "Command.Item.Saved", value)
                }
                "delete" -> ItemRepository.removeItem(value)?.let {
                    TLocale.sendTo(player, "Command.Item.Deleted", value)
                }
            }
        }
    }

    private fun toJson(player: Player, itemStack: ItemStack) {
        val json = Items.toJson(itemStack)
        if (json.length < 200) TLocale.sendTo(player, "Command.Item.To-Json", json)
        else Paster.paste(player, json, "json")
    }

    private fun fromJson(player: Player, json: String) {
        CronusUtils.addItem(player, Items.fromJson(json))
    }

}
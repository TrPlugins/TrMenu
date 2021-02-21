package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.util.lite.Materials
import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType

/**
 * @author Arasple
 * @date 2021/2/21 13:41
 */
class CommandTest : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        sender as Player

        val chest = ReceptacleAPI.createReceptacle(InventoryType.CHEST, "Def").also {
            it as InventoryChest
            it.rows = 3
        }

        chest.type.totalSlots.forEach { chest.setItem(Materials.values().random().parseItem(), it) }
        chest.open(sender)

        val task = Tasks.timer(20, 10, false) {
            chest.title = (0..20).random().toString()
        }
        Tasks.delay(20 * 20) {
            task.cancel()
        }
    }

}
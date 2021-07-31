package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import taboolib.common.platform.*
import taboolib.library.xseries.XMaterial

/**
 * @author Arasple
 * @date 2021/2/21 13:41
 */
object CommandTest : CommandExpresser {

    override val command = subCommand {
        // menu test
        execute<Player> { player, _, _ ->
            val chest = ReceptacleAPI.createReceptacle(InventoryType.CHEST, "Def").also {
                it as InventoryChest
                it.rows = 3
            }

            chest.type.totalSlots.forEach { chest.setItem(XMaterial.values().random().parseItem(), it) }
            chest.open(player)

            val task = Tasks.timer(20, 10, false) {
                chest.title = (0..20).random().toString()
            }
            Tasks.delay(20 * 20) {
                task.cancel()
            }
        }
    }

}
package me.arasple.mc.trmenu.module.internal.command.impl

import me.arasple.mc.trmenu.module.internal.command.CommandExpression
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.library.xseries.XMaterial
import taboolib.module.ui.receptacle.ChestInventory
import taboolib.module.ui.receptacle.createReceptacle

/**
 * @author Arasple
 * @date 2021/2/21 13:41
 */
object CommandTest : CommandExpression {

    override val command = subCommand {
        // menu test
        execute<Player> { player, _, _ ->
            val chest = InventoryType.CHEST.createReceptacle("Def").also {
                it as ChestInventory
                it.rows = 3
            }

            chest.type.totalSlots.forEach { chest.setItem(XMaterial.values().random().parseItem(), it) }
            chest.open(player)

            val task = submit(delay = 20, period = 10, async = false) {
                chest.title = (0..20).random().toString()
            }
            submit(delay = (20 * 20)) {
                task.cancel()
            }
        }
    }

}
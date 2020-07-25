package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.api.factory.task.ClickTask
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/23 17:23
 */
class CommandListMenu : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val page = 0

        val indexs = mutableMapOf<Int, String>()

        if (sender !is Player) {
            return
        }

        TrMenuAPI.buildMenu()
            .title("ListMenus")
            .layout(
                "########C",
                "|       |",
                "|       |",
                "|       |",
                "|       |",
                "####F####"
            )
            .build { e ->
                val session = e.session
                val has = arrayOf(page > 0, false)
                val menus = Menu.getMenus()

                has[1] = menus.size > 28
                if (has[0]) session.setItem(45, ItemStack(Material.CYAN_STAINED_GLASS_PANE))
                if (has[1]) session.setItem(53, ItemStack(Material.LIME_STAINED_GLASS_PANE))

                for (i in menus.indices) {
                    val slot = Items.INVENTORY_CENTER[i]
                    val menu = menus[i]
                    indexs[slot] = menu.id
                    session.setItem(
                        slot, ItemBuilder(Material.PAPER).name(menu.id).build()
                    )
                }
            }
            .click { e ->
                indexs[e.slot]?.let {
                    Tasks.run {
                        TrMenuAPI.getMenuById(it)?.open(sender, 0, MenuOpenEvent.Reason.PLAYER_COMMAND)
                    }
                }
                return@click ClickTask.Action.CANCEL_MODIFY
            }
            .close {
                sender.playSound(sender.location, Sound.BLOCK_CHEST_CLOSE, 1f, 1f)
            }
            .display(sender) {
                sender.playSound(sender.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
            }

    }

}
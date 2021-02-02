package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 22:25
 */
class ActionClose : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        player.getSession().close(closePacket = true, updateInventory = true)
    }

    companion object {

        private val name = "close|shut".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionClose()
        }

        val registery = name to parser

    }

}
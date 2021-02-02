package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 21:22
 */
class ActionDelay : AbstractAction() {

    fun getDelay(player: Player) = parseContent(player).toLongOrNull() ?: 0L

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        TODO("Not yet implemented")
    }

    companion object {

        private val name = "delay|wait".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ -> ActionDelay() }

        val registery = name to parser

    }

}
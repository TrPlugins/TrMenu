package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/2 11:05
 */
class ActionUpdate(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.getSession()

        if (baseContent.isBlank() || baseContent.equals("update", true)) {
            session.activeIcons.forEach {
                it.onReset(session)
                it.settingItem(session, it.getProperty(session))
            }
        } else {
            baseContent.split(";").mapNotNull { session.getIcon(it) }.forEach {
                it.onReset(session)
                it.settingItem(session, it.getProperty(session))
            }
        }
    }

    companion object {

        private val name = "(icon)?-?update".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionUpdate(value.toString(), option)
        }

        val registery = name to parser

    }

}
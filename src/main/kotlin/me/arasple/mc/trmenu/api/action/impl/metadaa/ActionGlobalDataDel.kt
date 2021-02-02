package me.arasple.mc.trmenu.api.action.impl.metadaa

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 * key support regex
 * del-data: KEY
 */
class ActionGlobalDataDel(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val data = Metadata.globalData

        parseContentSplited(placeholderPlayer, ";").forEach { it ->
            val regex = Regex(it)
            data.getKeys(true).filter { it.matches(regex) }.forEach { data.set(it, null) }
        }
    }

    companion object {

        private val name = "(remove|rem|del)-?(global|g)(temp|var(iable)?|meta)s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionGlobalDataDel(value.toString(), option)
        }

        val registery = name to parser

    }

}
package me.arasple.mc.trmenu.api.action.impl.metadaa

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 *
 * set-meta: KEY VALUE
 */
class ActionMetaSet(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer, ";").forEach {
            val split = it.split(" ", limit = 2)
            if (split.size == 2) {
                val key = split[0]
                val value = split[1]

                Metadata.getMeta(player)[key] = value
                // TODO DEBUG
            }
        }
    }

    companion object {

        private val name = "set-?(temp|var(iable)?|meta)s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionMetaSet(value.toString(), option)
        }

        val registery = name to parser

    }

}
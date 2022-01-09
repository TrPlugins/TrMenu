package trplugins.menu.api.action.impl.metadaa

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.internal.data.Metadata
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 *
 * set-meta: KEY VALUE
 */
class ActionMetaSet(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(adaptPlayer(placeholderPlayer), ";").forEach {
            val split = it.split(" ", limit = 2)
            if (split.size == 2) {
                val key = split[0]
                val value = split[1]

                Metadata.getMeta(player)[key] = value
                // TODO DEBUG
            }
        }
    }

    companion object : ActionDesc {

        override val name = "set-?(temp|var(iable)?|meta)s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionMetaSet(value.toString(), option)
        }

    }

}
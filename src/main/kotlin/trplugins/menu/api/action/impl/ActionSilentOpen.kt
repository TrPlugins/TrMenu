package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.internal.data.Metadata
import org.bukkit.entity.Player
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/2/9 12:23
 */
class ActionSilentOpen(val open: ActionOpen, option: ActionOption) : InternalAction(option = option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Metadata.setBukkitMeta(player, "FORCE_OPEN")
        open.onExecute(player)
    }

    companion object : ActionDesc {

        override val name = "(force|silent)-?(open|trplugins.menu)".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSilentOpen(ActionOpen(value.toString(), option), option)
        }

    }

}
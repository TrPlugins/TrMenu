package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/9 12:23
 */
class ActionSilentOpen(val open: ActionOpen, option: ActionOption) : AbstractAction(option = option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Metadata.setBukkitMeta(player, "FORCE_OPEN")
        open.onExecute(player)
    }

    companion object {

        private val name = "(force|silent)-?(open|menu)".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSilentOpen(ActionOpen(value.toString(), option), option)
        }

        val registery = name to parser

    }

}
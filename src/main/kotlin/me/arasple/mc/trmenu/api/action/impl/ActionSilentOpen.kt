package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/9 12:23
 */
class ActionSilentOpen(val open: ActionOpen) : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Metadata.setBukkitMeta(player, "FORCE_OPEN")
        open.run(player)
    }

    companion object {

        private val name = "(force|silent)-?(open|menu)".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSilentOpen(ActionOpen(value.toString(), option))
        }

        val registery = name to parser

    }

}
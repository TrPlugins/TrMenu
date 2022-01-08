package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/9 12:23
 */
class ActionSilentClose : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Metadata.setBukkitMeta(player, "FORCE_CLOSE")
        player.session().close(closePacket = true, updateInventory = true)
    }

    companion object {

        private val name = "(force|silent)-?(close|shut)".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionSilentClose()
        }

        val registery = name to parser

    }

}
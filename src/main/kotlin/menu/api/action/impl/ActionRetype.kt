package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 21:04
 */
class ActionRetype : InternalAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        Metadata.setBukkitMeta(player, "RE_ENTER")
    }

    companion object : ActionDesc {

        override val name = "re-?(peat|catcher|input|enter|type)s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionRetype()
        }

    }

}

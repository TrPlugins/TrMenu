package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/5/3 20:47
 */
class ActionReloadInventory : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        submit(delay = 2) {
            player.session().playerItemSlots()
        }
    }

    companion object : ActionDesc {

        override val name = "(reload|rl)-?inv(entory)?s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { _, _ -> ActionReloadInventory() }

    }

}
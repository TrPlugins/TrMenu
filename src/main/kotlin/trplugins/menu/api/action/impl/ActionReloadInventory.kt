package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/5/3 20:47
 */
class ActionReloadInventory : InternalAction() {

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
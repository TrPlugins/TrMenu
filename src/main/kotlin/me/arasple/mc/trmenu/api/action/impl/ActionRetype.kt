package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

/**
 * @author Arasple
 * @date 2021/1/31 21:04
 */
class ActionRetype : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        player.setMetadata("RE_ENTER", FixedMetadataValue(TrMenu.plugin, "___"))
    }

    companion object {

        private val name = "re-?(peat|catcher|input|enter|type)s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { _, _ ->
            ActionRetype()
        }

        val registery = name to parser

    }

}

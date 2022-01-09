package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 11:38
 */
class ActionCommandConsole(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        {
            parseContentSplited(placeholderPlayer, ";").forEach {
                console().performCommand(it)
            }
        }.also {
            if (Bukkit.isPrimaryThread()) {
                it.invoke()
            } else {
                submit(async = false) {
                    it.invoke()
                }
            }
        }
    }

    companion object : ActionDesc {

        override val name = "console".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionCommandConsole(value.toString(), option)
        }

    }

}
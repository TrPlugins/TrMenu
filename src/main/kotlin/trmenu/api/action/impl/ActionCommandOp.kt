package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.Bukkit.dispatchCommand
import taboolib.common.platform.function.submit
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 11:38
 */
class ActionCommandOp(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        {
            parseContentSplited(placeholderPlayer, ";").forEach {
                player.isOp.let { isOp ->
                    player.isOp = true
                    try {
                        dispatchCommand(player, it)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                    player.isOp = isOp
                }
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

        override val name = "op(erator)?s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionCommandOp(value.toString(), option)
        }

    }

}

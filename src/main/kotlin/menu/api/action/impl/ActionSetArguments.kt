package menu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import trmenu.util.Regexs
import trmenu.util.collections.Variables
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc
import trmenu.module.display.session

/**
 * @author Arasple
 * @date 2021/2/5 8:55
 */
class ActionSetArguments(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val args = Variables(baseContent, Regexs.SENTENCE) {
            it[1]
        }.element.flatMap {
            val v = parse(adaptPlayer(placeholderPlayer), it.value)
            if (it.isVariable) listOf(v)
            else v.split(" ")
        }.filterNot { it.isBlank() }

        Metadata.setBukkitMeta(player, "FORCE_ARGS")
        player.session().arguments = args.toTypedArray()
    }

    companion object : ActionDesc {

        override val name = "set-?arg(ument)?s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetArguments(value.toString(), option)
        }

    }

}
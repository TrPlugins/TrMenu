package menu.api.action.impl.metadaa

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.data.Metadata
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import trmenu.api.action.InternalAction
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 19:58
 *
 * set-meta: KEY VALUE
 */
class ActionGlobalDataSet(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(adaptPlayer(placeholderPlayer), ";").forEach {
            val split = it.split(" ", limit = 2)
            if (split.size == 2) {
                val key = split[0]
                val value = split[1]

                Metadata.globalData[key] = value
            }
        }
    }

    companion object : ActionDesc {

        override val name = "set-?(global|g)-?datas?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionGlobalDataSet(value.toString(), option)
        }

    }

}
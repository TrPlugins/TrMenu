package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.internal.script.js.JavaScriptAgent
import org.bukkit.entity.Player
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/1/31 11:43
 */
class ActionJavaScript(content: String, option: ActionOption) : InternalAction(content, option) {

    init {
        JavaScriptAgent.preCompile(baseContent)
    }

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        JavaScriptAgent.eval(player.session(), baseContent)
    }

    companion object : ActionDesc {

        override val name = "((java)?-?script|js)s?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionJavaScript(value.toString(), option)
        }

    }

}
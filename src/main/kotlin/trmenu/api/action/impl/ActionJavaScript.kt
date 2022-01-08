package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.module.internal.script.js.JavaScriptAgent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 11:43
 */
class ActionJavaScript(content: String, option: ActionOption) : AbstractAction(content, option) {

    init {
        JavaScriptAgent.preCompile(baseContent)
    }

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        JavaScriptAgent.eval(player.session(), baseContent)
    }

    companion object {

        private val name = "((java)?-?script|js)s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionJavaScript(value.toString(), option)
        }

        val registery = name to parser

    }

}
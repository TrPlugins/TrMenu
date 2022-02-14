package trplugins.menu.api.action.impl.script

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session
import trplugins.menu.module.internal.script.js.JavaScriptAgent

/**
 * TrMenu
 * trplugins.menu.api.action.impl.script.JavaScript
 *
 * @author Score2
 * @since 2022/02/14 12:52
 */
class JavaScript(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "((java)?-?script|js)s?".toRegex()

    override fun readContents(contents: Any): ActionContents {
        JavaScriptAgent.preCompile(contents.toString())
        return super.readContents(contents)
    }

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        JavaScriptAgent.eval(player.session(), contents.stringContent())
    }

}
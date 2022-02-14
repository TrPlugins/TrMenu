package trplugins.menu.module.internal.script

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.TrMenuAPI
import trplugins.menu.util.EvalResult
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.display.session
import trplugins.menu.module.internal.script.js.JavaScriptAgent

/**
 * TrMenu
 * trmenu.module.internal.script.Script
 *
 * @author Score2
 * @since 2022/01/09 17:48
 */

fun EvalResult.asItemStack(): ItemStack? {
    return any as ItemStack?
}

// in Condition.Companion
fun MenuSession.evalScript(script: String?): EvalResult {
    return placeholderPlayer.evalScript(script)
}

// in Condition
fun String.evalScript(session: MenuSession): EvalResult {
    return if (isEmpty()) EvalResult.TRUE
    else session.placeholderPlayer.evalScript(this)
}

fun ProxyPlayer.evalScript(script: String?) =
    cast<Player>().evalScript(script)

fun Player.evalScript(script: String?): EvalResult {
    script ?: return EvalResult(null)
    val (isJavaScript, js) = JavaScriptAgent.serialize(script)
    return if (isJavaScript) JavaScriptAgent.eval(MenuSession.getSession(this), js!!)
    else TrMenuAPI.instantKether(this, script)
}
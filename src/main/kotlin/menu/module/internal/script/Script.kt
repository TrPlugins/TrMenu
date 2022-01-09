package menu.module.internal.script

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyPlayer
import trmenu.api.TrMenuAPI
import trmenu.util.EvalResult
import trmenu.module.display.MenuSession
import trmenu.module.display.session
import trmenu.module.internal.script.js.JavaScriptAgent
import menu.util.function.ContentParser
import menu.util.function.ScriptParser

/**
 * TrMenu
 * trmenu.module.internal.script.Script
 *
 * @author Score2
 * @since 2022/01/09 17:48
 */
val scriptParser = menu.util.function.ScriptParser { player, script ->
    player.evalScript(script)
}
val contentParser = menu.util.function.ContentParser { player, content ->
    player.session().parse(content)
}

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
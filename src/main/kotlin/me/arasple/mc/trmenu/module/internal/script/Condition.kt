package me.arasple.mc.trmenu.module.internal.script

import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.script.js.JavaScriptAgent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/25 11:13
 */
inline class Condition(private val script: String) {

    fun eval(session: MenuSession): EvalResult {
        return if (script.isEmpty()) EvalResult.TRUE
        else eval(session.placeholderPlayer, script)
    }

    companion object {

        fun eval(session: MenuSession, script: String): EvalResult {
            return eval(session.placeholderPlayer, script)
        }

        fun eval(player: Player, script: String): EvalResult {
            val js = script.startsWith("js: ").also { script.removePrefix("js: ") }
            return if (js) JavaScriptAgent.eval(MenuSession.getSession(player), script)
            else TrMenuAPI.instantKether(player, script)
        }

    }

}
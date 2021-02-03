package me.arasple.mc.trmenu.module.internal.script.js

import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.script.EvalResult

/**
 * @author Arasple
 * @date 2021/2/3 8:34
 */
data class ScriptFunction(val id: String, private val raw: String) {


    private val hasFunction = raw.contains("function")
    private val hasReturn = raw.contains("return")
    private val basement = if (hasFunction) raw else "function def() { ${if (hasReturn) "" else "return"} $raw } def()"

    fun compile(session: MenuSession, args: List<String>): EvalResult {
        val array = "var args = new Array(${args.joinToString("\", \"", "\"", "\"")})"
        val content = "$array\n$basement"

        return JavaScriptAgent.eval(
            session,
            content
        )
    }

}
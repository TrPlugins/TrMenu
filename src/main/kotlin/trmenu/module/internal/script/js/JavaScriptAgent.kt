package trmenu.module.internal.script.js

import com.google.common.collect.Maps
import trmenu.module.display.MenuSession
import trmenu.module.internal.script.EvalResult
import org.bukkit.Bukkit
import taboolib.common5.compileJS
import java.util.function.Function
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.SimpleBindings
import javax.script.SimpleScriptContext

/**
 * @author Arasple
 * @date 2021/1/31 11:44
 */
object JavaScriptAgent {

    private val prefixes = arrayOf(
        "js: ",
        "$ ",
    )

    private val bindings = mapOf(
        "bukkitServer" to Bukkit.getServer(),
        "utils" to Assist.INSTANCE
    )

    private val compiledScripts = Maps.newConcurrentMap<String, CompiledScript>()

    fun serialize(script: String): Pair<Boolean, String?> {
        prefixes.firstOrNull { script.startsWith(it) }?.let {
            return true to script.removePrefix(it)
        }
        return false to null
    }

    fun preCompile(script: String): CompiledScript {
        return compiledScripts.computeIfAbsent(script) {
            script.compileJS()
        }
    }

    fun eval(session: MenuSession, script: String, cacheScript: Boolean = true): EvalResult {
        val context = SimpleScriptContext()

        context.setBindings(SimpleBindings(bindings).also {
            it["session"] = session
            it["player"] = session.viewer
            it["sender"] = session.viewer
        }, ScriptContext.ENGINE_SCOPE)
        val setAttribute: (String, Function<Any, Any?>) -> Unit = { name, func ->
            context.setAttribute(name, func, ScriptContext.ENGINE_SCOPE)
        }

        setAttribute(
            "vars", java.util.function.Function<Any, Any?> { session.parse(it.toString()) },
        )
        setAttribute(
            "varInt", java.util.function.Function<Any, Any?> { session.parse(it.toString()).toIntOrNull() ?: 0 },
        )
        setAttribute(
            "varDouble",
            java.util.function.Function<Any, Any?> { session.parse(it.toString()).toDoubleOrNull() ?: 0.0 },
        )
        setAttribute(
            "funInt", java.util.function.Function<Any, Any?> { session.parse("{$it}").toIntOrNull() ?: 0 },
        )
        setAttribute(
            "funDouble", java.util.function.Function<Any, Any?> { session.parse("{$it}").toIntOrNull() ?: 0 },
        )

        val compiledScript =
            if (cacheScript) preCompile(script)
            else script.compileJS()

        return EvalResult(compiledScript?.eval(context))

    }

}
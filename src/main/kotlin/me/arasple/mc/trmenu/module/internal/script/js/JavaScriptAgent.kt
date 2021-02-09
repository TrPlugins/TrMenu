package me.arasple.mc.trmenu.module.internal.script.js

import com.google.common.collect.Maps
import io.izzel.taboolib.util.Features
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.script.EvalResult
import org.bukkit.Bukkit
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

    private val bindings = mapOf(
        "bukkitServer" to Bukkit.getServer(),
        "utils" to Assist.INSTANCE
    )

    private val compiledScripts = Maps.newConcurrentMap<String, CompiledScript>()

    fun preCompile(script: String): CompiledScript {
        return compiledScripts.computeIfAbsent(script) {
            Features.compileScript(script)
        }
    }

    fun eval(session: MenuSession, script: String, cacheScript: Boolean = true): EvalResult {
        val context = SimpleScriptContext()

        context.setBindings(SimpleBindings(bindings).also {
            it["session"] = session
            it["player"] = session.viewer
        }, ScriptContext.ENGINE_SCOPE)
        val setAttribute: (String, Function<Any, Any?>) -> Unit = { name, func ->
            context.setAttribute(name, func, ScriptContext.ENGINE_SCOPE)
        }

        setAttribute(
            "vars", java.util.function.Function<Any, Any?> { session.parse(it.toString()) },
        )
        setAttribute(
            "varInt", java.util.function.Function<Any, Any?> { session.parse(it.toString()).toIntOrNull() },
        )
        setAttribute(
            "varDouble", java.util.function.Function<Any, Any?> { session.parse(it.toString()).toDoubleOrNull() },
        )

        val compiledScript =
            if (cacheScript) preCompile(script)
            else Features.compileScript(script)

        return EvalResult(compiledScript?.eval(context))

    }

}
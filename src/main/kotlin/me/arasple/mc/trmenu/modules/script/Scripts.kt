package me.arasple.mc.trmenu.modules.script

import me.arasple.mc.trmenu.modules.expression.ExpressionHandler
import me.arasple.mc.trmenu.modules.script.ScriptUtils.translate
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.function.Function
import javax.script.*

/**
 * @author Arasple
 * @date 2020/3/24 10:50
 */
object Scripts {

    private val bindings = SimpleBindings()
    private val engine: ScriptEngine = ScriptEngineManager(null).getEngineByName("nashorn")
    private val compiledScripts = mutableMapOf<String, CompiledScript>()

    init {
        bindings["bukkitServer"] = Bukkit.getServer()
//        bindings["utils"] = TrUtils.INST
    }

    fun expression(player: Player, expression: String) = script(player, ExpressionHandler.parseExpression(expression), bindings)

    fun script(player: Player, script: String) = script(player, script, bindings)

    fun script(player: Player, script: String, bindings: SimpleBindings) = script(player, script, bindings, false)

    fun script(player: Player, script: String, bindings: SimpleBindings, silent: Boolean) = eval(player, compile(script), bindings, silent)

    private fun compile(content: String): CompiledScript = compiledScripts.computeIfAbsent(content) {
        val script = translate(content)
        Msger.debug("PRE-COMPILE-SCRIPT", content, script)
        (engine as Compilable).compile(script)
    }

    private fun eval(player: Player, script: CompiledScript, bindings: SimpleBindings) = eval(player, script, bindings, false)

    private fun eval(player: Player, script: CompiledScript, bindings: SimpleBindings, silent: Boolean): ScriptResult = try {
        val content = SimpleScriptContext()
        content.setBindings(bindings, ScriptContext.ENGINE_SCOPE)
        content.setAttribute(ScriptUtils.function, Function<String, String> { Msger.replace(player, it) }, ScriptContext.ENGINE_SCOPE)
        ScriptResult(script.eval(content))
    } catch (e: Throwable) {
        if (!silent) Msger.printErrors("SCRIPT", e, script.toString())
        ScriptResult()
    }


}
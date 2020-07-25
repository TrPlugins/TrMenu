package me.arasple.mc.trmenu.modules.script

import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.api.events.MenuCloseEvent
import me.arasple.mc.trmenu.data.MetaPlayer.replaceWithMeta
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.modules.expression.Expressions
import me.arasple.mc.trmenu.modules.script.utils.AssistUtils
import me.arasple.mc.trmenu.modules.script.utils.ScriptUtils
import me.arasple.mc.trmenu.modules.script.utils.ScriptUtils.translate
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
        bindings["TrUtils"] = AssistUtils.INSTANCE
        bindings["utils"] = AssistUtils.INSTANCE
    }

    fun expression(player: Player, expression: String) = script(player, Expressions.parseExpression(expression), bindings, true)

    fun script(player: Player, script: String, cache: Boolean) = script(player, script, bindings, cache)

    fun script(player: Player, script: String, bindings: SimpleBindings, cache: Boolean) = script(player, script, bindings, false, cache)

    fun script(player: Player, script: String, bindings: SimpleBindings, silent: Boolean, cache: Boolean) = eval(player, script, compile(script, cache), bindings, silent)

    private fun compile(content: String, cache: Boolean): CompiledScript? {
        return try {
            if (cache) {
                compiledScripts.computeIfAbsent(content) {
                    val script = translate(content)
                    Msger.debug("PRE-COMPILE-SCRIPT", content, script)
                    (engine as Compilable).compile(script)
                }
            } else (engine as Compilable).compile(content)
        } catch (e: Throwable) {
            null
        }
    }

    private fun eval(player: Player, rawScript: String, script: CompiledScript?, bindings: SimpleBindings) = eval(player, rawScript, script, bindings, false)

    private fun eval(player: Player, rawScript: String, script: CompiledScript?, bindings: SimpleBindings, silent: Boolean): ScriptResult = try {
        val content = SimpleScriptContext()
        content.setBindings(SimpleBindings(bindings).let {
            it["player"] = player
            return@let it
        }, ScriptContext.ENGINE_SCOPE)
        content.setAttribute(ScriptUtils.function, Function<String, String> {
            player.replaceWithMeta(Msger.replace(player, it))
        }, ScriptContext.ENGINE_SCOPE)
        ScriptResult(script!!.eval(content))
    } catch (e: Throwable) {
        if (!silent) Msger.printErrors("SCRIPT", e, rawScript)
        cancel(player)
        ScriptResult()
    }

    private fun cancel(player: Player) {
        player.getMenuSession().menu?.let {
            it.close(player, 0, MenuCloseEvent.Reason.ERROR, true, silent = false)
            Sounds.BLOCK_ANVIL_BREAK.playSound(player)
        }
    }


}
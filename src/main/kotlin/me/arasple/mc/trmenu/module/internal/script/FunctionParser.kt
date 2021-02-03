package me.arasple.mc.trmenu.module.internal.script

import io.izzel.taboolib.kotlin.Indexed
import io.izzel.taboolib.kotlin.kether.KetherShell
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.script.js.JavaScriptAgent
import me.arasple.mc.trmenu.util.Utils
import me.arasple.mc.trmenu.util.collections.Variables
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 17:17
 */
object FunctionParser {

    private val functionPattern = "\\$?\\{(\\w+)s?: ?(.+?)}".toRegex()
    private val internalFunctionPattern = "\\\$\\{(.+?)\\}".toRegex()

    fun parse(player: Player, input: String): String {
        if (!Utils.containsPlaceholder(input)) return input
        val session = MenuSession.getSession(player)

        val functionParsed = Variables(input, functionPattern) { "${it[1]}:${it[2]}" }.element.joinToString("") {
            if (it.isVariable) {
                val split = it.value.split(":", limit = 2)
                val value = split[1]

                when (split[0].toLowerCase()) {
                    "kether", "ke" -> parseKetherFunction(player, value)
                    "javascript", "js" -> parseJavaScript(session, value)
                    "meta" -> Metadata.getMeta(player)[value].toString()
                    "data" -> Metadata.getData(player)[value].toString()
                    "globaldata", "gdata" -> Metadata.globalData.get(value).toString()
                    else -> "___ UNKNOWN $split ___"
                }
            } else it.value
        }

        return Variables(functionParsed, internalFunctionPattern) {
            it[1]
        }.element.joinToString("") {
            if (it.isVariable) parseInternalFunction(session, it.value)
            else it.value
        }
    }

    private fun parseInternalFunction(session: MenuSession, input: String): String {
        val func = input.split("_")

        session.menu?.settings?.internalFunctions?.forEach {
            if (it.id == func[0]) {
                val args = Indexed.subList(func, 1, func.size - 1)
                return it.compile(session, args).asString()
            }
        }
        return "___ UNKNOWN_FUNCTION_$input ___"
    }

    private fun parseKetherFunction(player: Player, input: String): String {
        return KetherShell.eval(input) { this.sender = player }.toString()
    }

    private fun parseJavaScript(session: MenuSession, input: String): String {
        return JavaScriptAgent.eval(session, input).asString()
    }

}
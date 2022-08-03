package trplugins.menu.module.internal.script

import trplugins.menu.api.TrMenuAPI
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.script.js.JavaScriptAgent
import trplugins.menu.util.Regexs
import trplugins.menu.util.collections.Variables
import trplugins.menu.util.ignoreCase
import trplugins.menu.util.print
import org.bukkit.entity.Player
import taboolib.common.util.subList
import taboolib.module.configuration.Configuration
import trplugins.menu.module.internal.hook.HookPlugin

/**
 * @author Arasple
 * @date 2021/1/31 17:17
 */
object FunctionParser {

    private val functionPattern = "\\$?\\{(\\w+)s?: ?(.+?[^\\\\}])}".toRegex()
    private val internalFunctionPattern = "\\$\\{([^0-9].+?[^\\\\}])}".toRegex()

    fun parse(player: Player, input: String, block: (type: String, value: String) -> String? = { _, value -> "{$value}" }): String {
        return runCatching {
            if (!Regexs.containsPlaceholder(input)) return input
            val session = MenuSession.getSession(player)

            val functionParsed = Variables(input, functionPattern) { "${it[1]}:${it[2]}" }.element.joinToString("") {
                if (it.isVariable) {
                    val split = it.value.split(":", limit = 2)
                    if (split.size < 2) return@joinToString it.value
                    val value = split[1]

                    when (val type = split[0].removePrefix(" ").lowercase()) {
                        "kether", "ke" -> parseKetherFunction(player, value)
                        "javascript", "js" -> parseJavaScript(session, value)
                        "meta", "m" -> Metadata.getMeta(player)[value].toString()
                        "data", "d" -> Metadata.getData(player)[value].toString()
                        "globaldata", "gdata", "g" -> runCatching { Metadata.globalData[value].toString() }.getOrElse { "null" }
                        "lang", "triton" -> parseLangText(player, value)

                        else -> block(type, value) ?: "{${it.value}}"
                    }
                } else it.value
            }

            return Variables(functionParsed, internalFunctionPattern) {
                it[1]
            }.element.joinToString("") {
                if (it.isVariable) parseInternalFunction(session, it.value)
                else it.value
            }
        }.onFailure {
            it.print("Error occured when parsing the string for player ${player.name}: $input")
        }.getOrElse { input }
    }

    private fun parseInternalFunction(session: MenuSession, input: String): String {
        val func = input.split("_")

        session.menu?.settings?.internalFunctions?.forEach {
            if (it.id == func[0]) {
                val args = subList(func, 1, func.size)
                return it.compile(session, args).asString()
            }
        }
        return "___ UNKNOWN_FUNCTION_$input ___"
    }

    private fun parseKetherFunction(player: Player, input: String): String {
        return TrMenuAPI.instantKether(player, input).asString()
    }

    private fun parseJavaScript(session: MenuSession, input: String): String {
        return JavaScriptAgent.eval(session, input).asString()
    }

    private fun parseLangText(player: Player, text: String): String {
        val split = text.split("=", limit = 2)
        return HookPlugin.getTriton().getText(player, split[0], if (split.size < 2) emptyArray() else split[1].split("_||_").toTypedArray()).toString()
    }

}

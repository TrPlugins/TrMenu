package trmenu.module.internal.script

import trmenu.api.TrMenuAPI
import trmenu.module.display.MenuSession
import trmenu.module.internal.data.Metadata
import trmenu.module.internal.script.js.JavaScriptAgent
import trmenu.util.Regexs
import trmenu.util.collections.Variables
import trmenu.util.ignoreCase
import trmenu.util.print
import org.bukkit.entity.Player
import taboolib.common.util.subList
import taboolib.module.configuration.Configuration

/**
 * @author Arasple
 * @date 2021/1/31 17:17
 */
object FunctionParser {

    private val functionPattern = "\\$?\\{(\\w+)s?: ?(.+?[^\\\\}])}".toRegex()
    private val internalFunctionPattern = "\\$\\{([^0-9].+?[^\\\\}])}".toRegex()

    fun parse(player: Player, input: String, section: Configuration? = null): String {
        return runCatching {
            if (!Regexs.containsPlaceholder(input)) return input
            val session = MenuSession.getSession(player)

            val functionParsed = Variables(input, functionPattern) { "${it[1]}:${it[2]}" }.element.joinToString("") {
                if (it.isVariable) {
                    val split = it.value.split(":", limit = 2)
                    if (split.size < 2) return@joinToString it.value
                    val value = split[1]

                    when (split[0].removePrefix(" ").lowercase()) {
                        "kether", "ke" -> parseKetherFunction(player, value)
                        "javascript", "js" -> parseJavaScript(session, value)
                        "meta", "m" -> Metadata.getMeta(player)[value].toString()
                        "data", "d" -> Metadata.getData(player)[value].toString()
                        "globaldata", "gdata", "g" -> try {
                            Metadata.globalData.get(value).toString()
                        } catch (t: Throwable) {
                            "null"
                        }
                        "node", "nodes", "n" -> section?.get(section.ignoreCase(value)).toString()
                        else -> "{${it.value}}"
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

}
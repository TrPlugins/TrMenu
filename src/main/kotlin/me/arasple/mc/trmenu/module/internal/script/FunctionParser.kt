package me.arasple.mc.trmenu.module.internal.script

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

    fun parse(player: Player, input: String): String {
        if (!Utils.containsPlaceholder(input)) return input

        return Variables(input, functionPattern) {
            "${it[1]}:${it[2]}"
        }.element.joinToString("") {
            if (it.isVariable) {
                val split = it.value.split(":", limit = 2)
                val value = split[1]
                when (split[0].toLowerCase()) {
                    "kether", "ke" -> parseKetherFunction(player, value)
                    "javascript", "js" -> parseJavaScript(player, value)
                    "meta" -> Metadata.getMeta(player)[value].toString()
                    "data" -> Metadata.getData(player)[value].toString()
                    "globalData", "gdata" -> Metadata.globalData.get(value).toString()
                    "test" -> {
                        arrayOf("")
                        ""
                    }
                    else -> "___ UNKNOWN__${split[0]}:$value ___"
                }
            } else {
                it.value
            }
        }
    }

    private fun parseKetherFunction(player: Player, input: String): String {
        return KetherShell.eval(input) { this.sender = player }.toString()
    }

    private fun parseJavaScript(player: Player, input: String): String {
        return JavaScriptAgent.eval(MenuSession.getSession(player), input).asString()
    }

}
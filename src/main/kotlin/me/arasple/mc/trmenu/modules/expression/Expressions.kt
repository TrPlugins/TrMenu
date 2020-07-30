package me.arasple.mc.trmenu.modules.expression

import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.utils.Msger

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 * hasMoney.100 and hasPoints.15
 */
object Expressions {

    val CACHED_PARSED = mutableMapOf<String, String>()

    fun parseExpression(string: String): String = CACHED_PARSED.computeIfAbsent(string) {
        var expression = TLocale.Translate.setColored(string).replace(" and ", " && ").replace(" or ", " || ")
        expression.split(" && ", " || ").forEach { part ->
            var type = part.split(".")[0] + "."
            val negtive = type.startsWith("!").also { type = type.removePrefix("!") }
            Expression.values().firstOrNull { it.regex.matches(type) }?.let { it ->
                val parsed = it.parse(part.split(".", limit = 2).let {
                    if (it.size > 1) it[1]
                    else ""
                })
                expression = expression.replace(part, if (negtive) "!($parsed)" else parsed)
            }
        }
        Msger.debug("EXPRESSION", string, expression)
        return@computeIfAbsent expression
    }

}
package me.arasple.mc.trmenu.modules.function.script.expression

import me.arasple.mc.trmenu.util.Msger

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 * hasMoney.100 and hasPoints.15
 */
object Expressions {

    val CACHED_PARSED = mutableMapOf<String, String>()

    fun parseExpression(string: String) = CACHED_PARSED.computeIfAbsent(string) {
        string
            .replace(" and ", " && ")
            .replace(" or ", " || ").let { ex ->
                var e = ex
                e.split(" && ", " || ").forEach { part ->
                    var type = part.split(".")[0] + "."
                    val negtive = type.startsWith("!").also { type = type.removePrefix("!") }
                    Expression.values().firstOrNull { it.regex.matches(type) }?.let { it ->
                        val parsed = it.parse(part.split(".", limit = 2).let {
                            if (it.size > 1) it[1]
                            else ""
                        })
                        e = e.replace(part, if (negtive) "!($parsed)" else parsed)
                    }
                }
                Msger.debug("EXPRESSION", string, e)
                return@computeIfAbsent e
            }
    }

}
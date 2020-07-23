package me.arasple.mc.trmenu.modules.expression

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 * hasMoney.100 and hasPoints.15
 */
object Expressions {

    val cachedParsed = mutableMapOf<String, String>()

    fun parseExpression(string: String): String = cachedParsed.computeIfAbsent(string) {
        var expression = string.replace(" and ", " && ").replace(" or ", " || ")
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
        expression
    }

}
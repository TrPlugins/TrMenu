package me.arasple.mc.trmenu.modules.expression

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 * hasMoney.100 and hasPoints.15
 */
object Expressions {

    val cachedParsed = mutableMapOf<String, String>()

    fun parseExpression(string: String): String = cachedParsed.computeIfAbsent(string) {
        val type = string.split(".")[0]
        var expression = string.replace(" and ", " && ").replace(" or ", " || ")
        expression.split("&&", "||").forEach { part ->
            Expression.values().firstOrNull { it.regex.matches(type) }?.let {
                expression = expression.replace(part, it.parse(part))
            }
        }
        expression
    }

}
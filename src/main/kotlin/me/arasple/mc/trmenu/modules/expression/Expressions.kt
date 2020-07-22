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
            val type = part.split(".")[0] + "."
            val negtive = type.startsWith("!")
            Expression.values().firstOrNull {
                it.regex.matches(type.removePrefix("!"))
            }?.let {
                val parsed = it.parse(part)
                expression = expression.replace(part, if (negtive) "!($parsed)" else parsed)
            }
        }
        expression
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(
            parseExpression("isNumber.111 and hasPerm.trmenu.use and !is.{REASON}.PLAYER_COMMAND")
        )
    }

}
package me.arasple.mc.trmenu.modules.expression

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.TrMenu
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.configuration.MemorySection

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 * hasMoney.100 and hasPoints.15
 */
object ExpressionHandler {

    private val settings = TConfig.create(TrMenu.plugin, "modules/expressions.yml").listener { reload(Bukkit.getConsoleSender()) }
    private val expressions = mutableListOf<Expression>()
    private val cachedParsed = mutableMapOf<String, String>()

    fun reload() = reload(null)

    private fun reload(sender: CommandSender?) {
        val start = System.currentTimeMillis()
        expressions.clear()
        settings.getValues(false).forEach { (_, section) ->
            if (section is MemorySection) {
                val regex = section.getString("regex")
                val replace = section.getString("replace")
                val splitBy = section.getString("splitBy", ".")
                if (regex != null && replace != null && splitBy != null) {
                    expressions.add(Expression(regex, replace, splitBy))
                }
            }
        }
        if (expressions.isNotEmpty() && sender != null) {
            cachedParsed.clear()
            TLocale.sendTo(sender, "LOADER.EXPRESSIONS", expressions.size, System.currentTimeMillis() - start)
        }
    }

    fun parseExpression(string: String): String = cachedParsed.computeIfAbsent(string) {
        val type = string.split(".")[0]
        var expression = string.replace(" and ", " && ").replace(" or ", " || ")
        expression.split("&&", "||").forEach { part ->
            val node = expressions.firstOrNull { it.regex.matches(type) }
            if (node != null) {
                expression = expression.replace(part, node.parse(part))
            }
        }
        expression
    }

    fun getArguments(string: String): Array<String> {
        val args = string.split(".").toMutableList()
        args.removeAt(0)
        return args.toTypedArray()
    }

}
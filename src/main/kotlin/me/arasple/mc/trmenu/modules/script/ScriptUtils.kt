package me.arasple.mc.trmenu.modules.script

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.utils.Patterns
import me.clip.placeholderapi.PlaceholderAPI

/**
 * @author Arasple
 * @date 2020/4/4 13:48
 */
object ScriptUtils {

    const val function = "replaceWithPlaceholders"

    fun translate(string: String): String {
        var content = string
        val matcher = PlaceholderAPI.getPlaceholderPattern().matcher(content)
        while (matcher.find()) {
            val find = matcher.group()
            val group = escape(Strings.replaceWithOrder(escapeMath(find), *getArgs(find)))
            content = content.replace(Regex("(['\"])?${escape(find)}(['\"])?"), "$function(\'$group\')")
        }
        val bracket = PlaceholderAPI.getBracketPlaceholderPattern().matcher(content)
        while (bracket.find()) {
            val group = escape(bracket.group())
            if (NumberUtils.isParsable(group.removeSurrounding("\\{", "\\}")))
                content = content.replace(Regex("(['\"])?${group}(['\"])?"), "$function(\'$group\')")
        }
        val funs = Patterns.INTERNAL_FUNCTION.matcher(content)
        while (funs.find()) {
            val group = escape(funs.group())
            content = content.replace(Regex("(['\"])?${group}(['\"])?"), "$function(\'$group\')")
        }
        return content
    }

    private fun getArgs(content: String): Array<String> {
        val replaces = mutableListOf<String>()
        val bracker = PlaceholderAPI.getBracketPlaceholderPattern().matcher(content)
        var size = -1
        while (bracker.find()) size = size.coerceAtLeast(NumberUtils.toInt(bracker.group().removeSurrounding("{", "}"), -1))
        for (i in 0..size) replaces.add("{trmenu_args_$i}")
        return replaces.toTypedArray()
    }

    private fun escape(string: String): String = escapeMath(
        string
            .replace("{", "\\{")
            .replace("}", "\\}")
            .replace("[", "\\[")
            .replace("]", "\\]")
    )

    private fun escapeMath(string: String): String = string
        .replace("+", "\\+")
        .replace("*", "\\*")

}
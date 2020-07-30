package me.arasple.mc.trmenu.modules.script.utils

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.Strings
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/4/4 13:48
 */
object ScriptUtils {

    const val function = "rwp"
    val argumentPattern: Pattern = Pattern.compile("[$]?\\{(.*?)}")
    val placeholderPattern: Pattern = Pattern.compile("[%]([^%]+)[%]")
    val bracketPlaceholderPattern: Pattern = Pattern.compile("[{]([^{}]+)[}]")

    fun translate(string: String): String {
        var content = string
        placeholderPattern.matcher(content).let {
            while (it.find()) {
                val find = it.group()
                val group = escape(Strings.replaceWithOrder(escapeMath(find), *getArgs(find)))
                content = replaceFind(content, escape(find), group)
            }
        }
        argumentPattern.matcher(content).let {
            while (it.find()) {
                val group = it.group(1)
                val find = it.group()
                val isFunction = find.startsWith("$")
                if (!isFunction && !group.startsWith("meta") && !group.startsWith("input") && !NumberUtils.isParsable(group)) {
                    continue
                }
                content = replaceFind(content, escape(find))
            }
        }
        return content
    }

    private fun replaceFind(string: String, find: String): String = replaceFind(string, find, find)

    private fun replaceFind(string: String, find: String, group: String): String = string.replace("['\"]?(\\$)?$find['\"]?".toRegex(), "$function(\'$group\')")

    private fun getArgs(content: String): Array<String> {
        val replaces = mutableListOf<String>()
        val bracker = bracketPlaceholderPattern.matcher(content)
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
            .replace("$", "\\$")
    )

    private fun escapeMath(string: String): String = string
        .replace("+", "\\+")
        .replace("*", "\\*")

}
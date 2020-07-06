package me.arasple.mc.trmenu.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/7/6 16:49
 */
enum class Nodes(regex: String) {

    TITLE("<(?i)(title)[:=]( )?(.+>)"),

    SUBTITLE("<(?i)(sub(-)?title)[:=]( )?(.+>)"),

    FADEIN("<(?i)((fade)?(-)?in)[:=]( )?(.+>)"),

    STAY("<(?i)stay[:=]( )?(.+>)"),

    FADEOUT("<(?i)((fade)?(-)?out)[:=]( )?(.+>)"),

    CHANCE("<(?i)(c|chance|rate):( )?([0-9]+[.]?[0-9]*>)"),

    DELAY("<(?i)(d|delay|wait):( )?([0-9]+[.]?[0-9]*>)"),

    PLAYERS("<(?i)((p|(for|all)?(-)?players))(:)?(.+)?>"),

    REQUIREMENT("<(?i)(r|require(ment)?|condition):( )?(.+>)");

    private var pattern: Array<Pattern> = arrayOf(Pattern.compile("$regex>"), Pattern.compile(regex))

    fun matcher(content: String): Matcher {
        val matcher = pattern[0].matcher(content)
        return if (matcher.find()) {
            pattern[1].matcher(matcher.group())
        } else {
            pattern[1].matcher(content)
        }
    }

    companion object {

        fun read(string: String): Pair<String, Map<Nodes, String>> {
            var content = string
            val result = mutableMapOf<Nodes, String>()
            Nodes.values().forEach {
                val matcher = it.matcher(content)
                if (matcher.find()) {
                    val group = matcher.group()
                    val args = group.removeSuffix(">").split(delimiters = *arrayOf("?", "=", "~", ":"), limit = 2)
                    result[it] = if (args.size >= 2) args[1] else ""
                    content = content.replace(group, "")
                }
            }
            return Pair(content, result)
        }

    }

}
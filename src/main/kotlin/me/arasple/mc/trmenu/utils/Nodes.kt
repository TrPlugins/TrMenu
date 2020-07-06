package me.arasple.mc.trmenu.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/5/30 12:42
 */
class Nodes(var pattern: Array<Pattern>) {

    constructor(regex: String) : this(arrayOf(Pattern.compile("$regex<"), Pattern.compile(regex)))

    fun matcher(content: String): Matcher {
        val matcher = pattern[0].matcher(content)
        return if (matcher.find()) pattern[1].matcher(matcher.group()) else pattern[1].matcher(content)
    }

    companion object {

        val ACTION_TITLE = mapOf(
                Pair("TITLE", Nodes("<(?i)(title)[:=]( )?(.+>)")),
                Pair("SUBTITLE", Nodes("<(?i)(sub(-)?title)[:=]( )?(.+>)")),
                Pair("FADEIN", Nodes("<(?i)((fade)?(-)?in)[:=]( )?(.+>)")),
                Pair("STAY", Nodes("<(?i)stay[:=]( )?(.+>)")),
                Pair("FADEOUT", Nodes("<(?i)((fade)?(-)?out)[:=]( )?(.+>)"))
        )

        val ACTION_CATCHER = mapOf(
                Pair("TYPE", Nodes("<(?i)(type)[:=]( )?(.+>)")),
                Pair("BEFORE", Nodes("<(?i)(before)[:=]( )?(.+>)")),
                Pair("VALID", Nodes("<(?i)(valid)[:=]( )?(.+>)")),
                Pair("INVALID", Nodes("<(?i)(in(-)?valid)[:=]( )?(.+>)")),
                Pair("CANCEL", Nodes("<(?i)(cancel)[:=]( )?(.+>)")),
                Pair("REQUIREMENT", Nodes("<(?i)(r|require(ment)?|condition)[:=]( )?(.+>)"))
        )

    }

}
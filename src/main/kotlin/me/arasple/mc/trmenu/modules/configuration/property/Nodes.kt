package me.arasple.mc.trmenu.modules.configuration.property

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/7/6 16:49
 */
enum class Nodes(regex: String) {

    MAT_REPOSITORY("<repo(sitory)?(s)?:(.+)?>"),

    MAT_HEAD("<((player|variable)?(-)?head):(.+)?>"),

    MAT_TEXTURED_SKULL("<(((custom|texture)?(-)?skull)|custom-head)(:)?(.+)?>"),

    MAT_DATA_VALUE("<(((data|id)?(-)?value)|data|value)(:)?([0-9]+[.]?[0-9]*>)"),

    MAT_MODEL_DATA("<((model(-)?(value|data)))(:)?([0-9]+[.]?[0-9]*>)"),

    MAT_DYE_LEATHER("<dye(-)?(leather)?:( )?([0-9]+[,]+[0-9]+[,]+[0-9]*>)"),

    MAT_BANNER("<banner(-)?(dye|color|style)?:( )?(.+>)"),

    MAT_HEAD_DATABASE("<((head(-)?(database))|(hdb)):( )?(([0-9]|random)+>)"),

    MAT_ORAXEN("<oraxen(s)?:(.+)?>"),

    MAT_HEAD_SKINSRESTORER("<skin(s)?(-)?restorer(s)?:(.+)?>"),

    MAT_SCRIPT("<((javascript|js)?(-)?(item)?):(.+)?>"),

    MAT_JSON("<JSON>"),

    MAT_VARIABLE("<VARIABLE>"),

    MAT_ORIGINAL("<ORIGINAL>"),

    TITLE("<(title)[:=]( )?(.+?>)"),

    SUBTITLE("<(sub(-)?title)[:=]( )?(.+?>)"),

    FADEIN("<((fade)?(-)?in)[:=]( )?([0-9]+>)"),

    STAY("<stay[:=]( )?([0-9]+>)"),

    FADEOUT("<((fade)?(-)?out)[:=]( )?([0-9]+>)"),

    CHANCE("<(c|chance|rate)[:=]( )?([0-9]+[.]?[0-9]*>)"),

    DELAY("<(d|delay|wait)[:=]( )?([0-9]+[.]?[0-9]*>)"),

    PLAYERS("<((p|(for|all)?(-)?players))[:=]?(.+)?>"),

    TYPE("<(type)[:=]( )?(.+>)"),

    BEFORE("<(before)[:=]( )?(.+>)"),

    VALID("<(valid)[:=]( )?(.+>)"),

    INVALID("<(in(-)?valid)[:=]( )?(.+>)"),

    CANCEL("<(cancel|rate)[:=]( )?(.+>)"),

    REQUIREMENT("<(r|require(ment)?|condition)[:=]( )?(.+>)");

    private var pattern: Array<Pattern> = arrayOf(Pattern.compile("(?i)$regex<"), Pattern.compile("(?i)$regex"))

    fun matcher(content: String): Matcher {
        val matcher = pattern[0].matcher(content)
        return if (matcher.find())
            pattern[0].matcher(content)
        else
            pattern[1].matcher(content)

    }

    fun isMatType() = this.name.startsWith("MAT_")

    companion object {

        fun read(string: String, vararg nodes: Nodes): Pair<String, Map<Nodes, String>> {
            var content = string
            val result = mutableMapOf<Nodes, String>()
            (if (nodes.isEmpty()) values() else nodes).forEach {
                val matcher = it.matcher(content)
                if (matcher.find()) {
                    val group = matcher.group().removeSuffix("<")
                    val args = group.removeSuffix(">").split(delimiters = arrayOf("?", "=", "~", ":"), limit = 2)
                    result[it] = if (args.size >= 2) args[1] else ""
                    content = content.replace(group, "")
                }
            }
            return Pair(content, result)
        }

    }

}
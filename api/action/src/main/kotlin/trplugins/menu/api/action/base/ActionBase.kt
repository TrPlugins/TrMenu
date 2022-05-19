package trplugins.menu.api.action.base

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.util.random
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.util.Regexs
import java.util.function.Consumer

/**
 * TrMenu
 * trplugins.menu.api.action.base.ActionBase
 *
 * @author Score2
 * @since 2022/02/08 21:36
 */
abstract class ActionBase(val handle: ActionHandle) {

    val name: String = javaClass.simpleName
    val lowerName = name.lowercase()
    val flatName by lazy { lowerName.replace(Regex("(?!^)([A-Z])"), "-\$1") }
    private val defRegex = "${name.replace("(?!^)([A-Z])".toRegex(), ".*?\$1").lowercase()}.*?".toRegex()
    open val regex get() = defRegex

    fun alias(vararg alias: String): Regex {
        val regexString = defRegex.toString()
        alias.forEach {
            if (it.contains("-")) {
                regexString.plus("|${it.split("-").joinToString(".*?", postfix = ".*?")}")
            } else {
                regexString.plus("|$it.*?")
            }
        }
        return regexString.toRegex()
    }

    fun isParsable(baseContent: String) =
        Regexs.containsPlaceholder(baseContent) || baseContent.contains("&")

    fun ProxyPlayer.parse(content: String) =
        handle.placeholderParser.apply(this, content)

    fun String.parse(player: ProxyPlayer) =
        player.parse(this)

    fun ProxyPlayer.parseContent(baseContent: String) =
        if (isParsable(baseContent))
            parse(baseContent)
        else
            baseContent

    fun String.parseContent(player: ProxyPlayer) =
        player.parseContent(this)

    fun ProxyPlayer.parseContentSplited(baseContent: String, vararg delimiters: String = arrayOf("\\n", "\\r")) =
        parseContent(baseContent).split(*delimiters)

    fun String.parseContentSplited(player: ProxyPlayer, vararg delimiters: String = arrayOf("\\n", "\\r")) =
        player.parseContentSplited(this, *delimiters)

    open fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer = player) {

    }

    open fun readContents(contents: Any) =
        ActionContents(contents)

    fun ofEntry(contents: Any? = null, option: Option = Option()) =
        ActionEntry(this, readContents(contents ?: ""), option)

    fun ofOption(string: String): Pair<String, Option> {
        var content = string
        val options = mutableMapOf<OptionType, String>()

        OptionType.values().forEach {
            it.regex.find(content)?.let { find ->
                val value = find.groupValues.getOrElse(it.group) { "" }
                options[it] = value
                content = it.regex.replace(content, "")
            }
        }

        return content.removePrefix(" ") to Option(options)
    }

    fun register() {
        handle.register(this)
    }

    fun unregister() {
        handle.unregister(this)
    }

    inner class Option(val set: Map<OptionType, String> = mapOf()) {

        fun evalChance(): Boolean {
            return if (!set.containsKey(OptionType.CHANCE)) true else random(set[OptionType.CHANCE]!!.toDoubleOrNull() ?: 0.0)
        }

        fun evalDelay(): Long {
            return if (!set.containsKey(OptionType.DELAY)) -1L else set[OptionType.DELAY]!!.toLongOrNull() ?: -1L
        }

        fun evalPlayers(defPlayer: ProxyPlayer, action: Consumer<ProxyPlayer>) =
            evalPlayers(defPlayer) { action.accept(it) }

        fun evalPlayers(defPlayer: ProxyPlayer, action: (ProxyPlayer) -> Unit) {
            if (set.containsKey(OptionType.PLAYERS)) {
                val cond = set[OptionType.PLAYERS].toString()
                onlinePlayers().forEach {
                    if (cond.isBlank()) action(it)
                    else if (handle.conditionParser.apply(it, cond).asBoolean(false)) action(it)
                }
            } else {
                action.invoke(defPlayer)
            }
        }

        fun evalCondition(player: ProxyPlayer): Boolean {
            return if (!set.containsKey(OptionType.CONDITION)) {
                true
            } else {
                set[OptionType.CONDITION]?.let {
                    handle.conditionParser.apply(player, it).asBoolean()
                } ?: false
            }
        }
    }

    enum class OptionType(val regex: Regex, val group: Int) {

        DELAY("[{<](delay|wait)[=:] ?([0-9]+)[}>]", 2),

        CHANCE("[{<](chance|rate|rand(om)?)[=:] ?([0-9.]+)[}>]", 3),

        CONDITION("[{<](condition|requirement)[=:] ?(.+)[}>]", 2),

        PLAYERS("[{<]players[=:]? ?(.*)[}>]", 1);

        constructor(regex: String, group: Int) : this("(?i)$regex".toRegex(), group)

    }
}
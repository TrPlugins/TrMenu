package trplugins.menu.api.action.base

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.Actions
import trplugins.menu.api.action.Actions.Companion.contentParser
import trplugins.menu.util.Regexs

/**
 * @author Arasple
 * @date 2021/1/29 17:52
 */
abstract class AbstractAction(
    val baseContent: String = "",
    var option: ActionOption = ActionOption(),
) {
    val parsable = Regexs.containsPlaceholder(baseContent) || baseContent.contains("&")

    fun assign(player: ProxyPlayer){
        Actions.runAction(player, listOf(this))
    }

    fun parse(player: ProxyPlayer, string: String): String {
        return contentParser.parse(player, string)
    }

    fun parseContent(player: ProxyPlayer): String {
        return if (parsable) parse(player, baseContent) else baseContent
    }

    fun parseContentSplited(player: ProxyPlayer, vararg delimiters: String = arrayOf("\\n", "\\r")): List<String> {
        return parseContent(player).split(*delimiters)
    }

    fun run(player: ProxyPlayer) {
        val delay = option.evalDelay()
        if (!option.evalCondition(player)) return

        val proceed = { option.evalPlayers(player) { onExecute(it, player) } }
        if (delay > 0) submit(delay = delay) { proceed.invoke() }
        else proceed.invoke()
    }

    abstract fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer = player)

    override fun toString(): String {
        return "${javaClass.simpleName.substring(6)}: $baseContent${option.set.entries.joinToString("") { "{${it.key.name.lowercase()}=${it.value}}" }}"
    }

}
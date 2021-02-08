package me.arasple.mc.trmenu.api.action.base

import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.util.Regexs
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 17:52
 */
abstract class AbstractAction(val baseContent: String = "", var option: ActionOption = ActionOption()) {

    val parsable = Regexs.containsPlaceholder(baseContent) || baseContent.contains("&")

    fun Player.getSession(): MenuSession {
        return MenuSession.getSession(this)
    }

    fun parse(player: Player, string: String): String {
        return player.getSession().parse(string)
    }

    fun parseContent(player: Player): String {
        return if (parsable) parse(player, baseContent) else baseContent
    }

    fun parseContentSplited(player: Player, vararg delimiters: String = arrayOf("\\n", "\\r")): List<String> {
        return parseContent(player).split(*delimiters)
    }

    fun run(player: Player) {
        val delay = option.evalDelay()
        if (!option.evalCondition(player)) return

        val proceed = { option.evalPlayers(player) { onExecute(it, player) } }
        if (delay > 0) Tasks.delay(delay) { proceed.invoke() }
        else proceed.invoke()
    }

    abstract fun onExecute(player: Player, placeholderPlayer: Player = player)

    override fun toString(): String {
        return "${javaClass.simpleName.substring(6)}: $baseContent${option.set.entries.joinToString("") { "{${it.key.name.toLowerCase()}=${it.value}}" }}"
    }

}
package me.arasple.mc.trmenu.modules.action.base

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.configuration.property.Nodes
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.modules.metrics.MetricsHandler
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/26 12:45
 */
abstract class Action(val name: Regex, internal var content: String, var options: MutableMap<Nodes, String>) {

    constructor(name: String) : this(Regex(name), "", mutableMapOf())

    fun run(player: Player) {
        MetricsHandler.increase(1)

        if (options.containsKey(Nodes.DELAY)) {
            val delay = evalDelay(player)
            if (delay > 0) {
                Tasks.delay(delay) {
                    if (options.containsKey(Nodes.PLAYERS)) Bukkit.getOnlinePlayers().filter { options[Nodes.PLAYERS]?.let { it1 -> Scripts.expression(it, it1).asBoolean() } as Boolean }.forEach { onExecute(it, Msger.replaceWithBracketPlaceholders(player, content)) }
                    else onExecute(player)
                }
            }
            return
        }
        if (options.containsKey(Nodes.PLAYERS)) Bukkit.getOnlinePlayers().filter { options[Nodes.PLAYERS]?.let { it1 -> Scripts.expression(it, it1).asBoolean() } as Boolean }.forEach { onExecute(it, Msger.replaceWithBracketPlaceholders(player, content)) }.also { return }

        onExecute(player)
    }

    private fun onExecute(player: Player, content: String?) {
        if (content != null) {
            val temp = content
            setContent(content).also { onExecute(player) }
            setContent(temp)
        } else onExecute(player)
    }

    fun evalDelay(player: Player) = NumberUtils.toLong(Msger.replace(player, options[Nodes.DELAY]), 0L)

    fun evalChance(player: Player) = options[Nodes.CHANCE]?.let { Numbers.random(NumberUtils.toDouble(Msger.replace(player, it), 1.0)) } ?: true

    fun evalCondition(player: Player) = options[Nodes.REQUIREMENT]?.let { Scripts.expression(player, it).asBoolean() } ?: true

    fun getSession(player: Player) = player.getMenuSession()

    fun getContentSplited(player: Player) = getContentSplited(player, "\\n", "\\r")

    fun getContentSplited(player: Player, vararg delimiters: String) = getContent(player).split(*delimiters)

    fun getSplitedBySemicolon(player: Player) = getContent(player).split(SEMICOLON)

    fun replaceWithSpaces(string: String) = string.replace("\\s", " ")

    fun getContent(player: Player): String = Msger.replace(player, content)

    fun getContent(): String = content

    open fun setContent(content: String) {
        this.content = content
    }

    open fun setContent(any: Any) {}

    open fun onExecute(player: Player) {}

    open fun newInstance(): Action = javaClass.getDeclaredConstructor().newInstance()

    fun toDetailedString(): String {
        return "Action(name=${javaClass.simpleName}, content='$content', options=$options)"
    }

    override fun toString(): String = buildString {
        append(this@Action.javaClass.simpleName.removePrefix("Action").toLowerCase())
        append(": ")
        append(getContent())
        append(buildString { options.forEach { append("<${it.key.name.toLowerCase()}: ${it.value}>") } })
    }

    companion object {

        val SEMICOLON = "( )?([;；])( )?".toRegex()

    }


}
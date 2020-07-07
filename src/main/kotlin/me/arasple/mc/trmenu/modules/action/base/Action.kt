package me.arasple.mc.trmenu.modules.action.base

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Nodes
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
        if (options.containsKey(Nodes.DELAY)) {
            val delay = NumberUtils.toLong(options[Nodes.DELAY], -1)
            if (delay > 0) {
                Tasks.runDelayTask(Runnable {
                    if (options.containsKey(Nodes.PLAYERS)) Bukkit.getOnlinePlayers().filter { options[Nodes.PLAYERS]?.let { it1 -> Scripts.script(it, it1).asBoolean() } as Boolean }.forEach { onExecute(it, Msger.replaceWithBracketPlaceholders(player, content)) }
                    else onExecute(player)
                }, delay)
            }
            return
        }
        if (options.containsKey(Nodes.PLAYERS)) Bukkit.getOnlinePlayers().filter { options[Nodes.PLAYERS]?.let { it1 -> Scripts.script(it, it1).asBoolean() } as Boolean }.forEach { onExecute(it, Msger.replaceWithBracketPlaceholders(player, content)) }.also { return }
        onExecute(player)
    }

    private fun onExecute(player: Player, content: String?) {
        if (content != null) {
            val temp = content
            setContent(content).also { onExecute(player) }
            setContent(temp)
        } else onExecute(player)
    }

    fun getContent(player: Player): String = Msger.replace(player, content)

    fun getContent(): String = content

    open fun setContent(content: String) {
        this.content = content
    }

    open fun onExecute(player: Player) {}

    open fun newInstance(): Action = javaClass.getDeclaredConstructor().newInstance()

    override fun toString(): String = buildString {
        append(javaClass.simpleName.removePrefix("Action").toLowerCase())
        append(": ")
        append(getContent())
        append(buildString { options.forEach { append("<${it.key.name.toLowerCase()}: ${it.value}>") } })
    }

}
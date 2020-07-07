package me.arasple.mc.trmenu.modules.action

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.action.impl.*
import me.arasple.mc.trmenu.modules.action.impl.hook.*
import me.arasple.mc.trmenu.modules.action.impl.item.ActionTakeItem
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Nodes
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/7 10:35
 */
object Actions {

    val cachedActions = mutableMapOf<String, List<Action>>()
    val registeredActions = mutableListOf(
        // hook
        ActionGiveMoney(),
        ActionGivePoints(),
        ActionSetMoney(),
        ActionSetPoints(),
        ActionTakeMoney(),
        ActionTakePoints(),
        // item
        ActionTakeItem(),
        // menu
//            ActionClose(),
//            ActionOpen(),
//            ActionRefresh(),
//            ActionRemoveTempVariable(),
//            ActionSetArgs(),
//            ActionSetPage(),
//            ActionSetSlots(),
//            ActionSetTempVariable(),
//            ActionSetTitle(),
//            ActionSilentClose(),
        // normal
        ActionChat(),
        ActionActionbar(),
//            ActionCatcher(),
        ActionCommand(),
        ActionCommandConsole(),
        ActionCommandOp(),
        ActionConnect(),
        ActionDelay(),
        ActionJavaScript(),
        ActionReturn(),
        ActionSound(),
        ActionTell()
//            ActionTellraw(),
//            ActionTitle()
    )

    fun registerAction(action: Action) = registeredActions.add(action.newInstance())

    fun runActions(player: Player, actions: List<Action>): Boolean {
        var delay: Long = 0
        loop@ for (action in actions.stream().filter {
            if (!Numbers.random(NumberUtils.toDouble(Msger.replace(player, it.options[Nodes.CHANCE]), 1.0))) return@filter false
            if (it.options.containsKey(Nodes.REQUIREMENT)) {
                val v = it.options[Nodes.REQUIREMENT]
                if (v != null && !(Scripts.expression(player, v).asBoolean())) return@filter false
            }
            return@filter true
        }) {
            when {
                action is ActionReturn -> return false
                action is ActionDelay -> delay += NumberUtils.toLong(action.getContent(player), 0)
                delay > 0 -> Tasks.runDelayTask(Runnable { action.run(player) }, delay)
                else -> Tasks.runTask(Runnable { action.run(player) })
            }
        }
        return true
    }

    fun runCachedAction(player: Player, action: String) = runActions(player, cachedActions.computeIfAbsent(action) { readActions(action) })

    fun readActions(strings: List<String>): List<Action> = mutableListOf<Action>().let { actions ->
        strings.forEach { if (it.isNotEmpty()) actions.addAll(readActions(it)) }
        return@let actions
    }

    fun readActions(strings: String): List<Action> {
        val actions = mutableListOf<Action>()
        val sharedOptions = mutableMapOf<Nodes, String>()
        strings.split("_||_", "#>>").forEach { it ->
            val name = it.replace(Regex("<.+>"), "").split(':')[0]
            val content = it.removePrefix(name).removePrefix(":")
            val action = registeredActions.firstOrNull { name.toLowerCase().matches(it.name) }?.newInstance() ?: ActionUnknow().also { it.setContent(strings) }

            if (content.isNotEmpty()) {
                val result = Nodes.read(content)
                action.setContent(result.first)
                action.options = result.second.toMutableMap()
            }

            action.options.forEach { (option, value) -> sharedOptions[option] = value }
            actions.add(action)
        }
        actions.forEach {
            sharedOptions.forEach { (option, value) ->
                if (!it.options.containsKey(option)) it.options[option] = value
            }
        }
        return actions
    }

    fun writeActions(actions: List<Action>): List<String> = mutableListOf<String>().let { list ->
        actions.forEach { list.add(it.toString()) }
        return@let list
    }

}
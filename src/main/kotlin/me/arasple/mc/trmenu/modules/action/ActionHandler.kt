package me.arasple.mc.trmenu.modules.action

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.action.base.Option
import me.arasple.mc.trmenu.modules.action.impl.*
import me.arasple.mc.trmenu.modules.action.impl.hook.*
import me.arasple.mc.trmenu.modules.action.impl.item.ActionTakeItem
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/26 12:44
 */
object ActionHandler {

    val cachedActions = mutableMapOf<String, List<Action>>()

    val actions = mutableListOf(
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

    fun registerAction(action: Action) = actions.add(action.newInstance())

//    fun readActions(strings: String): MutableList<Action> {
//        val actions = mutableListOf<Action>()
//        val sharedOptions = mutableMapOf<Option, String>()
//        strings.split("_||_", "#>>").forEach {
//            val read = ActionReader.readSingle(it)
//            read.options.forEach { (option, value) -> sharedOptions[option] = value }
//            actions.add(read)
//        }
//        actions.forEach {
//            sharedOptions.forEach { (option, value) ->
//                if (!it.options.containsKey(option)) it.options[option] = value
//            }
//        }
//        return actions
//    }

//    fun readActions(strings: List<String>): MutableList<Action> {
//        val actions = mutableListOf<Action>()
//        strings.forEach {
//            if (it.isNotEmpty()) actions.addAll(readActions(it))
//        }
//        return actions
//    }
//
//    fun writeActions(actions: List<Action>): MutableList<String> {
//        val strings = mutableListOf<String>()
//        actions.forEach { strings.add(writeAction(it)) }
//        return strings
//    }
//
//    fun writeAction(action: Action): String {
//        val type = action.javaClass.simpleName.removePrefix("Action").toLowerCase()
//        val contet = action.getContent()
//        val options = writeOptions(action.options)
//        return "$type: $contet$options"
//    }

    fun writeOptions(options: MutableMap<Option, String>): String {
        val string = ""
        options.forEach { string.plus("<${it.key.name.toLowerCase()}: ${it.value}>") }
        return string
    }

    fun runActions(player: Player, actions: List<Action>): Boolean = runActions(player, actions, null)

    fun runActions(player: Player, actions: List<Action>, placeholders: Map<String, String>?): Boolean {
        var delay: Long = 0
        loop@ for (action in actions.stream().filter { it ->
            if (!Numbers.random(NumberUtils.toDouble(it.options[Option.CHANCE], 1.0))) return@filter false
            if (it.options.containsKey(Option.REQUIREMENT)) {
                val v = it.options[Option.REQUIREMENT]
                if (v != null && !(Scripts.script(player, v).asBoolean())) return@filter false
            }
            return@filter true
        }) {
            when {
                action is ActionReturn -> return false
                action is ActionDelay -> delay += NumberUtils.toLong(action.getContent(player), 0)
                delay > 0 -> Tasks.runDelayTask(Runnable { action.run(player, placeholders) }, delay)
                else -> Tasks.runTask(Runnable { action.run(player, placeholders) })
            }
        }
        return true
    }

//    fun runCachedAction(player: Player, action: String) = runActions(player, cachedActions.computeIfAbsent(action) { readActions(action) })

}
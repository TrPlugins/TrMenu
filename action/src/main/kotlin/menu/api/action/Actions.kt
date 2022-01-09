package menu.api.action

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import trmenu.api.action.base.ActionDesc
import trmenu.api.action.impl.*
import menu.util.function.ContentParser
import menu.util.function.ScriptParser

/**
 * @author Arasple
 * @date 2021/1/29 17:51
 * TrMenu internal actions feature
 */
class Actions(val contentParser: menu.util.function.ContentParser, val scriptParser: menu.util.function.ScriptParser) {

    private val actionsBound = " ?(_\\|\\|_|&&&) ?".toRegex()
    private val registries = mutableListOf<ActionDesc>().also { list ->
        runningClasses.forEach { `class` ->
            val instance = `class`.getInstance(false)?.get() ?: return@forEach
            runCatching { instance.javaClass.asSubclass(ActionDesc::class.java) }.getOrNull() ?: return@forEach
            list.add(instance as ActionDesc)
        }
    }

    fun runAction(player: ProxyPlayer, actions: List<String>) {
        runAction(player, readAction(actions))
    }

    fun runAction(player: ProxyPlayer, actions: List<AbstractAction>): Boolean {
        val run = mutableListOf<AbstractAction>()
        var result = true
        var delay = 0L

        run filter@ {
            actions.filter { it.option.evalChance() }.forEach {
                when {
                    it is ActionReturn && it.option.evalCondition(player) -> {
                        result = false
                        return@filter
                    }
                    it is ActionDelay -> delay += it.getDelay(player)
                    delay > 0 -> submit(delay = delay) { it.run(player) }
                    else -> run.add(it)
                }
            }
        }
        run.forEach { it.run(player) }
        return result
    }

    /**
     * 读取多个对象动作
     */
    fun readAction(any: List<Any>): List<AbstractAction> {
        return any.flatMap { readAction(it) }
    }

    /**
     * 读取一个文本动作
     */
    fun readAction(any: Any): List<AbstractAction> {
        val actions = mutableListOf<AbstractAction>()
        val findParser: (String) -> (Any, ActionOption) -> AbstractAction = { name ->
            registries.find { it.registery.first.matches(name.lowercase()) }?.registery?.second ?: registries[0].registery.second
        }

        when (any) {
            is Map<*, *> -> {
                val entry = any.entries.firstOrNull() ?: return actions
                val key = entry.key.toString()
                val value = entry.value ?: return actions
                findParser(key).invoke(value, ActionOption()).let { actions.add(it) }
            }
            else -> {
                val loaded = any.toString().split(actionsBound).map {
                    val split = it.split(": ", limit = 2)
                    val parser = findParser(split[0])
                    val string = split.getOrElse(1) { split[0] }

                    run {
                        val (content, option) = ActionOption.of(string)
                        parser.invoke(content, option)
                    }
                }
                loaded.maxByOrNull { it.option.set.size }?.let { def ->
                    loaded.forEach { it.option = def.option }
                }
                actions.addAll(loaded)
            }
        }

        return actions
    }

    companion object {

        private var instance: menu.api.action.Actions? = null

        val contentParser get() = menu.api.action.Actions.Companion.instance!!.contentParser
        val scriptParser get() = menu.api.action.Actions.Companion.instance!!.scriptParser

        private fun checkInst() {
            menu.api.action.Actions.Companion.instance ?: error("Actions instance not initialized.")
        }

        fun init(contentParser: menu.util.function.ContentParser, scriptParser: menu.util.function.ScriptParser) {
            menu.api.action.Actions.Companion.instance = menu.api.action.Actions(contentParser, scriptParser)
        }

        fun runAction(player: ProxyPlayer, actions: List<String>) {
            menu.api.action.Actions.Companion.checkInst()
            return menu.api.action.Actions.Companion.instance!!.runAction(player, actions)
        }

        fun runAction(player: ProxyPlayer, actions: List<AbstractAction>): Boolean {
            menu.api.action.Actions.Companion.checkInst()
            return menu.api.action.Actions.Companion.instance!!.runAction(player, actions)
        }

        fun readAction(any: List<Any>): List<AbstractAction> {
            menu.api.action.Actions.Companion.checkInst()
            return menu.api.action.Actions.Companion.instance!!.readAction(any)
        }

        fun readAction(any: Any): List<AbstractAction> {
            menu.api.action.Actions.Companion.checkInst()
            return menu.api.action.Actions.Companion.instance!!.readAction(any)
        }

    }

}
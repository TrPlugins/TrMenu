package me.arasple.mc.trmenu.api.action

import io.izzel.taboolib.kotlin.Tasks
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.api.action.impl.*
import me.arasple.mc.trmenu.api.action.impl.hook.ActionMoneyAdd
import me.arasple.mc.trmenu.api.action.impl.hook.ActionMoneySet
import me.arasple.mc.trmenu.api.action.impl.hook.ActionMoneyTake
import me.arasple.mc.trmenu.api.action.impl.metadaa.*
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 17:51
 * TrMenu internal actions feature
 */
object Actions {

    private val actionsBound = " ?(_\\|\\|_|&&&) ?".toRegex()
    private val registries = mapOf(
        // Logic & Functional
        ActionReturn.registery,
        ActionDelay.registery,
        ActionJavaScript.registery,
        ActionKether.registery,
        // Bukkit
        ActionTell.registery,
        ActionChat.registery,
        ActionTitle.registery,
        ActionActionbar.registery,
        ActionCommand.registery,
        ActionCommandConsole.registery,
        ActionCommandOp.registery,
        ActionTellraw.registery,
        // BungeeCord
        ActionConnect.registery,
        // Menu
        ActionClose.registery,
        ActionOpen.registery,
        ActionPage.registery,
        ActionSetTitle.registery,
        ActionSound.registery,
        ActionMetaSet.registery,
        ActionMetaDel.registery,
        ActionDataSet.registery,
        ActionDataDel.registery,
        ActionGlobalDataSet.registery,
        ActionGlobalDataDel.registery,
        ActionRetype.registery,
        ActionRefresh.registery,
        // Hook
        ActionMoneySet.registery,
        ActionMoneyAdd.registery,
        ActionMoneyTake.registery,
        ActionTakeItem.registery,
    )

    fun runAction(player: Player, actions: List<String>) {
        runAction(player, readAction(actions))
    }

    fun runAction(player: Player, actions: List<AbstractAction>): Boolean {
        val run = mutableListOf<AbstractAction>()
        var delay = 0L

        actions.filter { it.option.evalChance() }.forEach {
            when {
                it is ActionReturn && it.option.evalCondition(player) -> {
                    return false
                }
                it is ActionDelay -> delay += it.getDelay(player)
                delay > 0 -> Tasks.delay(delay) { it.run(player) }
                else -> run.add(it)
            }
        }

        run.forEach { it.run(player) }
        return true
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
        val findParser: (String) -> ((Any, ActionOption) -> AbstractAction)? = { name ->
            registries.entries.find { it.key.matches(name) }?.value
        }

        when (any) {
            is Map<*, *> -> {
                val entry = any.entries.firstOrNull() ?: return actions
                val key = entry.key.toString()
                val value = entry.value ?: return actions
                findParser(key)?.invoke(value, ActionOption())?.let { actions.add(it) }
            }
            else -> {
                val loaded = any.toString().split(actionsBound).mapNotNull {
                    val split = it.split(":", limit = 2)
                    val parser = findParser(split[0])
                    val string = split.getOrElse(1) { "" }

                    if (parser != null) {
                        val (content, option) = ActionOption.of(string)
                        parser.invoke(content, option)
                    } else {
                        null
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

}
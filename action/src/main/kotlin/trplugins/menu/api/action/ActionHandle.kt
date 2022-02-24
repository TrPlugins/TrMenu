package trplugins.menu.api.action

import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import taboolib.common.reflect.Reflex.Companion.invokeConstructor
import taboolib.common.reflect.ReflexClass
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionEntry
import trplugins.menu.api.action.impl.logic.Break
import trplugins.menu.api.action.impl.logic.Delay
import trplugins.menu.util.EvalResult
import java.lang.reflect.Modifier
import java.util.function.BiFunction

/**
 * TrMenu
 * trplugins.menu.api.action.ActionHandle
 *
 * @author Score2
 * @since 2022/02/09 21:36
 */
class ActionHandle(
    val conditionParser: BiFunction<ProxyPlayer, String, EvalResult> =
        BiFunction { _, _ -> EvalResult.TRUE },
    val placeholderParser: BiFunction<ProxyPlayer, String, String> =
        BiFunction { _, s -> s },
    private val default: String = "tell"
) {

    val registries = mutableListOf<ActionBase>().also {
        runningClasses.forEach { `class` ->
            if (Modifier.isAbstract(`class`.modifiers)) return@forEach
            if (`class`.superclass != ActionBase::class.java) return@forEach

            it.add(`class`.asSubclass(ActionBase::class.java).invokeConstructor(this))
        }
    }

    fun registerActions(vararg bases: ActionBase) {
        bases.forEach { registries.add(it) }
    }

    fun getFindRegistered(key: String): ActionBase =
        registries.find { key.lowercase() == it.lowerName || it.regex.matches(key.lowercase()) } ?: defaultAction

    val defaultAction by lazy { registries.find { it.name.equals(default, true) }!! }

    fun runAction(player: ProxyPlayer, actions: List<String>) {
        runAction(player, ActionEntry.of(this, actions))
    }

    fun runAction(player: ProxyPlayer, actions: List<ActionEntry>): Boolean {
        val run = mutableListOf<ActionEntry>()
        var result = true
        var delay = 0L

        run filter@{
            actions.filter { it.option.evalChance() }.forEach {
                when {
                    it.base is Break && it.option.evalCondition(player) -> {
                        result = false
                        return@filter
                    }
                    it.base is Delay -> delay += it.base.getDelay(player, it.contents.stringContent())
                    delay > 0 -> submit(delay = delay) { it.execute(player) }
                    else -> run.add(it)
                }
            }
        }
        run.forEach { it.execute(player) }
        return result
    }

    companion object {

        val actionsBound = " ?(_\\|\\|_|&&&) ?".toRegex()

    }

}
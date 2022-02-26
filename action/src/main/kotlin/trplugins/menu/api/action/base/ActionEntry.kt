package trplugins.menu.api.action.base

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import trplugins.menu.api.action.ActionHandle

/**
 * TrMenu
 * trplugins.menu.api.action.base.ActionEntry
 *
 * @author Score2
 * @since 2022/02/08 22:11
 */
data class ActionEntry(
    val base: ActionBase,
    val contents: ActionContents,
    var option: ActionBase.Option,
) {
    val handle get() = base.handle

    fun assign(player: ProxyPlayer) {
        base.handle.runAction(player, listOf(this))
    }

    fun execute(player: ProxyPlayer) {
        val delay = option.evalDelay()
        if (!option.evalCondition(player)) return

        val proceed = { option.evalPlayers(player) { base.onExecute(contents, it, player) } }
        if (delay > 0) submit(delay = delay) { proceed.invoke() }
        else proceed.invoke()
    }


    companion object {

        fun of(handle: ActionHandle, any: List<Any>) =
            any.flatMap { of(handle, it) }

        fun of(handle: ActionHandle, any: Any): List<ActionEntry> {
            val entries = mutableListOf<ActionEntry>()

            when (any) {
                is Map<*, *> -> {
                    val entry = any.entries.firstOrNull() ?: return entries
                    val key = entry.key.toString()
                    val value = entry.value ?: return entries
                    val registered = handle.getRegisteredAction(key)

                    val actionEntry = registered.ofEntry(value, registered.Option())
                    entries.add(actionEntry)
                }
                else -> {
                    val loaded = any.toString().split(ActionHandle.actionsBound).map {
                        val split = it.split(": ", limit = 2)
                        val string = split.getOrElse(1) { split[0] }
                        val registered = handle.getRegisteredAction(split[0])

                        val (content, option) = registered.ofOption(string)
                        registered.ofEntry(content, option)
                    }
                    loaded.maxByOrNull { it.option.set.size }?.let { def ->
                        loaded.forEach { it.option = def.option }
                    }
                    entries.addAll(loaded)
                }
            }

            return entries
        }
    }
}
package me.arasple.mc.trmenu.api.action.base

import io.izzel.taboolib.kotlin.Randoms
import me.arasple.mc.trmenu.module.internal.script.Condition
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/29 17:56
 */
class ActionOption(val set: Map<Type, String> = mapOf()) {

    fun evalChance(): Boolean {
        return if (!set.containsKey(Type.CHANCE)) true else Randoms.Companion.random(set[Type.CHANCE]!!.toDoubleOrNull() ?: 0.0)
    }

    fun evalDelay(): Long {
        return if (!set.containsKey(Type.DELAY)) -1L else set[Type.DELAY]!!.toLongOrNull() ?: -1L
    }

    fun evalPlayers(defPlayer: Player, action: (Player) -> Unit) {
        if (set.containsKey(Type.PLAYERS)) {
            Bukkit.getOnlinePlayers().forEach {
                Condition.eval(it, set[Type.PLAYERS].toString())
            }
        } else {
            action.invoke(defPlayer)
        }
    }

    fun evalCondition(player: Player): Boolean {
        return if (!set.containsKey(Type.CONDITION)) {
            true
        } else {
            set[Type.CONDITION]?.let {
                Condition.eval(player, it).asBoolean()
            } ?: false
        }
    }

    companion object {

        fun of(string: String): Pair<String, ActionOption> {
            var content = string
            val options = mutableMapOf<Type, String>()

            Type.values().forEach {
                it.regex.find(content)?.let { find ->
                    val value = find.groupValues[it.group]
                    options[it] = value
                    content = it.regex.replace(content, "")
                }
            }

            return content.removePrefix(" ") to ActionOption(options)
        }

    }

    enum class Type(val regex: Regex, val group: Int) {

        DELAY("[{<](delay|wait)[=:] ?([0-9]+)[}>]", 2),

        CHANCE("[{<](chance|rate|rand(om)?)[=:] ?([0-9.]+)[}>]", 3),

        CONDITION("[{<](condition|requirement)[=:] ?(.+)[}>]", 2),

        PLAYERS("[{<]players?[=:] ?(.+)[}>]", 1);

        constructor(regex: String, group: Int) : this("(?i)$regex".toRegex(), group)

    }

}

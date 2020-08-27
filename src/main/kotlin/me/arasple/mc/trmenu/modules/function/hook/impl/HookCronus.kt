package me.arasple.mc.trmenu.modules.function.hook.impl

import ink.ptms.cronus.internal.condition.Condition
import ink.ptms.cronus.internal.condition.ConditionParser
import ink.ptms.cronus.internal.program.NoneProgram
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/8/27 19:49
 */
class HookCronus : HookInstance() {

    private val conditions = mutableMapOf<String, Condition>()
    private val programs = mutableMapOf<UUID, NoneProgram>()

    override fun getDepend(): String {
        return "Cronus"
    }

    override fun initialization() {
        TODO("Not yet implemented")
    }

    fun parseCondition(string: String) = conditions.computeIfAbsent(string) { ConditionParser.parse(string) }

    fun nonProgram(player: Player) = programs.computeIfAbsent(player.uniqueId) { NoneProgram(player) }

    fun reset(player: Player) {
        if (isHooked()) programs.remove(player.uniqueId)
    }

}
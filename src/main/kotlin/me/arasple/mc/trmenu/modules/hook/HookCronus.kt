package me.arasple.mc.trmenu.modules.hook

import ink.ptms.cronus.internal.condition.Condition
import ink.ptms.cronus.internal.condition.ConditionParser
import ink.ptms.cronus.internal.program.NoneProgram
import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/28 16:50
 */
object HookCronus {

    private const val PLUGIN_NAME = "Cronus"
    private var IS_HOOKED = false

    val conditions = mutableMapOf<String, Condition>()
    val programs = mutableMapOf<UUID, NoneProgram>()

    fun isHooked() = IS_HOOKED

    fun init() {
        IS_HOOKED = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)?.isEnabled ?: false
        if (isHooked()) {
            TLocale.sendToConsole("PLUGIN.HOOKED", PLUGIN_NAME)
        }
    }

    fun parseCondition(string: String) = conditions.computeIfAbsent(string) { ConditionParser.parse(string) }

    fun nonProgram(player: Player) = programs.computeIfAbsent(player.uniqueId) { NoneProgram(player) }

    fun reset(player: Player) {
        if (IS_HOOKED) programs.remove(player.uniqueId)
    }

}
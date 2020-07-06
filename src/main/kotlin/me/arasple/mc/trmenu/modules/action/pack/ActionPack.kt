package me.arasple.mc.trmenu.modules.action.pack

import me.arasple.mc.trmenu.modules.action.ActionHandler
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.script.Scripts
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/19 13:10
 */
data class ActionPack(val actions: MutableMap<String, MutableList<Action>>) {

    fun run(player: Player, placeholders: Map<String, String>) {
        actions.forEach {
            val condition = it.key
            val actions = it.value
            if (condition.isEmpty() || Scripts.script(player, condition).asBoolean()) {
                if (!ActionHandler.runActions(player, actions, placeholders)) {
                    return@forEach
                }
            }
        }
    }

}
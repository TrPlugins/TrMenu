package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/3 21:02
 */
class ActionChat : Action("chat") {

    override fun onExecute(player: Player) {
        Tasks.task {
            getContentSplited(player).forEach { player.chat(it) }
        }
    }

}
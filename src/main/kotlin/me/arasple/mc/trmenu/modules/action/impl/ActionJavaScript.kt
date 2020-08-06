package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/28 19:28
 */
class ActionJavaScript : Action("(java)?(-)?script(s)?|js") {

    var async: Boolean = false

    override fun onExecute(player: Player) {
        if (!async) {
            Tasks.task {
                Scripts.script(player, getContent(), true)
            }
        } else {
            Scripts.script(player, getContent(), true)
        }
    }

    private fun script(player: Player) {
        Scripts.script(player, getContent(), true)
    }

    override fun setContent(content: String) {
        async = content.endsWith("<async>")
        super.setContent(content.removePrefix("<async>"))
    }

}
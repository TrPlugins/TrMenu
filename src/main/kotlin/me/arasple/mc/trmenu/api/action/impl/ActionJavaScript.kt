package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.function.script.Scripts
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/28 19:28
 */
class ActionJavaScript : Action("(java)?(-)?script(s)?|js") {

    var async: Boolean = false

    override fun onExecute(player: Player) {
        val cache = TrMenu.SETTINGS.getBoolean("Menu.Action.Cache-JavaScripts")
        val content = if (cache) getContent() else getContent(player)

        if (!async) {
            Tasks.task {
                Scripts.script(player, content, cache)
            }
        } else Scripts.script(player, content, cache)
    }

    private fun script(player: Player) {
        Scripts.script(player, getContent(), true)
    }

    override fun setContent(content: String) {
        async = content.endsWith("<async>")
        super.setContent(content.removePrefix("<async>"))
    }

}
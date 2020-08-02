package me.arasple.mc.trmenu.modules.action.impl.data

import io.izzel.taboolib.module.db.local.LocalPlayer
import me.arasple.mc.trmenu.data.MetaPlayer.setMeta
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/1 22:40
 */
class ActionGlobalDataSet : Action("(set|edit)(-)?(file|global)(-)?(data)(s)?") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        val split = it.split(" ", limit = 2)
        if (split.size == 2) {
            val key = split[0]
            val value = replaceWithSpaces(split[1])

            LocalPlayer.get(player).set("TrMenu.Data.$key", value)
            player.setMeta("{data:$key}", value)
        }
    }

}
package me.arasple.mc.trmenu.modules.action.impl.menu

import io.izzel.taboolib.module.db.local.LocalPlayer
import me.arasple.mc.trmenu.data.MetaPlayer.setMeta
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionDataSet : Action("(set|edit)(-)?(data)(s)?") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        val split = it.split(" ", limit = 2)
        if (split.size == 2) {
            val key = split[0]
            val value = split[1]
            LocalPlayer.get(player).set("TrMenu.Data.$key", value)
            player.setMeta("{data:$key}", value)
        }
    }
}
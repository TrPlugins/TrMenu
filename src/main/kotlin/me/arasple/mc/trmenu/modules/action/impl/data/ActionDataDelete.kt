package me.arasple.mc.trmenu.modules.action.impl.data

import io.izzel.taboolib.module.db.local.LocalPlayer
import me.arasple.mc.trmenu.data.MetaPlayer.removeMeta
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionDataDelete : Action("(remove|rem|del)(-)?(data)(s)?") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach {
        LocalPlayer.get(player).set("TrMenu.Data.$it", null)
        player.removeMeta("{data:$it}")
    }

}
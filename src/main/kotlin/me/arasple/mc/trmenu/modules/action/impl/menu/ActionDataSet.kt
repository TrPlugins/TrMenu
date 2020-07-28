package me.arasple.mc.trmenu.modules.action.impl.menu

import io.izzel.taboolib.module.db.local.LocalPlayer
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionDataSet : Action("(set|edit)(-)?(data)(s)?") {

    override fun onExecute(player: Player) = getContentSplited(player, ";").forEach {
        val split = it.split(" ", limit = 2)
        if (split.size == 2) {
            LocalPlayer.get(player).set("TrMenu.Data.${split[0]}", split[1])
        }
    }
}
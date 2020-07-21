package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.data.MetaPlayer
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionMetaRemove : Action("(remove|rem|del)(-)?(temp|var(iable)?|meta)(s)?") {

    override fun onExecute(player: Player) = getContentSplited(player, ";").forEach { MetaPlayer.removeMeta(player, it) }

}
package me.arasple.mc.trmenu.api.action.impl.menu

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.base.Action
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

/**
 * @author Arasple
 * @date 2020/7/24 20:46
 */
class ActionReInput : Action("re(-)?(catcher|input|enter|type)(s)?") {

    override fun onExecute(player: Player) = player.setMetadata("RE_ENTER", FixedMetadataValue(TrMenu.plugin, ""))

}
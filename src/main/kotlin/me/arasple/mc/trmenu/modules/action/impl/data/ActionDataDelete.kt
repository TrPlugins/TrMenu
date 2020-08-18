package me.arasple.mc.trmenu.modules.action.impl.data

import io.izzel.taboolib.module.db.local.LocalPlayer
import me.arasple.mc.trmenu.api.Extends.removeMeta
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/18 22:10
 */
class ActionDataDelete : Action("(remove|rem|del)(-)?(data)(s)?") {

    override fun onExecute(player: Player) = getSplitedBySemicolon(player).forEach { it ->
        val locale = LocalPlayer.get(player)

        when {
            it.startsWith("^") -> {
                val match = it.removePrefix("^")
                val keys = locale.getConfigurationSection("TrMenu.Data")?.getKeys(true)
                keys?.filter { it.startsWith(match) }?.forEach {
                    locale.set("TrMenu.Data.$it", null)
                    player.removeMeta("{data:$it}")
                }
            }
            it.startsWith("$") -> {
                val match = it.removePrefix("$")
                val keys = locale.getConfigurationSection("TrMenu.Data")?.getKeys(true)
                keys?.filter { it.endsWith(match) }?.forEach {
                    locale.set("TrMenu.Data.$it", null)
                    player.removeMeta("{data:$it}")
                }
            }
            else -> {
                LocalPlayer.get(player).set("TrMenu.Data.$it", null)
                player.removeMeta("{data:$it}")
            }
        }
    }

}
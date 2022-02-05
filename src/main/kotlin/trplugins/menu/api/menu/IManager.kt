package trplugins.menu.api.menu

import org.bukkit.entity.Player
import trplugins.menu.api.receptacle.Receptacle

/**
 * @author Arasple
 * @date 2020/12/4 22:30
 */
interface IManager {

    fun getViewingReceptacle(player: Player): Receptacle?

    fun setViewingReceptacle(player: Player, receptacle: Receptacle? = null)

}
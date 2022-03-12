package trplugins.menu.api.menu

import org.bukkit.entity.Player
import trplugins.menu.api.receptacle.vanilla.window.WindowReceptacle

/**
 * @author Arasple
 * @date 2020/12/4 22:30
 */
interface IManager {

    fun getViewingReceptacle(player: Player): WindowReceptacle?

    fun setViewingReceptacle(player: Player, receptacle: WindowReceptacle? = null)

}
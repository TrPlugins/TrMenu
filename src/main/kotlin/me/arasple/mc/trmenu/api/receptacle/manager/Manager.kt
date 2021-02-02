package me.arasple.mc.trmenu.api.receptacle.manager

import me.arasple.mc.trmenu.api.menu.IManager
import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/12/4 22:30
 */
class Manager : IManager {

    private val cached = mutableMapOf<UUID, Receptacle?>()

    override fun getViewingReceptacle(player: Player): Receptacle? {
        return cached[player.uniqueId]
    }

    override fun setViewingReceptacle(player: Player, receptacle: Receptacle?) {
        cached[player.uniqueId] = receptacle
    }

}
package me.arasple.mc.trmenu.api.receptacle.window.handler

import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/17 10:51
 *
 * This take place only after the ReceptacleOpenEvent (if not cancelled)
 */
fun interface ReceptacleHandler {

    fun run(player: Player, receptacle: Receptacle)

}

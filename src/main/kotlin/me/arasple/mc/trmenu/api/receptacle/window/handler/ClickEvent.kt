package me.arasple.mc.trmenu.api.receptacle.window.handler

import me.arasple.mc.trmenu.api.event.ReceptacleInteractEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/17 10:51
 *
 * This take place the same time with ReceptacleInteractEvent
 */
fun interface ClickEvent {

    fun run(player: Player, event: ReceptacleInteractEvent)

}
